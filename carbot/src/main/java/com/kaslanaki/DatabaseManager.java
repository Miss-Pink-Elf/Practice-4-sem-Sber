package com.kaslanaki;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:h2:./car_data;DB_CLOSE_ON_EXIT=FALSE";
    private static final String DB_USER = "";
    private static final String DB_PASSWORD = "";

    private static final Map<String, String> FIELD_TO_COLUMN_MAP = new HashMap<>();
    static {
        FIELD_TO_COLUMN_MAP.put("chat_id", "chat_id");
        FIELD_TO_COLUMN_MAP.put("make", "make");
        FIELD_TO_COLUMN_MAP.put("model", "\"model\"");
        FIELD_TO_COLUMN_MAP.put("year", "\"year\"");
        FIELD_TO_COLUMN_MAP.put("mileage", "\"mileage\""); // Текущий пробег
        FIELD_TO_COLUMN_MAP.put("price", "\"price\"");
        FIELD_TO_COLUMN_MAP.put("photo_file_id", "photo_file_id");
        FIELD_TO_COLUMN_MAP.put("last_maint_date", "\"last_maint_date\"");
        FIELD_TO_COLUMN_MAP.put("last_maint_mileage", "\"last_maint_mileage\"");
        FIELD_TO_COLUMN_MAP.put("last_maint_description", "\"last_maint_description\"");
        FIELD_TO_COLUMN_MAP.put("last_maint_cost", "\"last_maint_cost\"");
    }

    public DatabaseManager() {
        System.out.println("[DB] DatabaseManager initialized. Calling initializeDatabase().");
        initializeDatabase();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private void closeConnection(Connection conn, PreparedStatement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConnection(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Инициализация базы данных
    private void initializeDatabase() {
        System.out.println("[DB] Initializing database...");
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();

            // Создание/изменение таблицы cars
            String createCarsTableSql = "CREATE TABLE IF NOT EXISTS cars (" +
                    "id INT PRIMARY KEY AUTO_INCREMENT," +
                    "chat_id BIGINT NOT NULL," +
                    "\"make\" VARCHAR(255) NOT NULL," +
                    "\"model\" VARCHAR(255) NOT NULL," +
                    "\"year\" INT," +
                    "\"mileage\" INT," +
                    "\"price\" DECIMAL(15, 2)," +
                    "photo_file_id VARCHAR(255)," +
                    "\"last_maint_date\" DATE," +
                    "\"last_maint_mileage\" INT," +
                    "\"last_maint_description\" VARCHAR(500)," +
                    "\"last_maint_cost\" DECIMAL(15, 2)" +
                    ")";
            System.out.println("[DB] Executing CREATE TABLE IF NOT EXISTS cars...");
            stmt.execute(createCarsTableSql);
            System.out.println("[DB] Table 'cars' checked/created with last maint fields."); // Обновленный лог

            // Создание индекса для таблицы cars
            String createCarsIndexSql = "CREATE INDEX IF NOT EXISTS idx_cars_chat_id ON cars (chat_id)";
            System.out.println("[DB] Executing CREATE INDEX IF NOT EXISTS idx_cars_chat_id...");
            stmt.execute(createCarsIndexSql);
            System.out.println("[DB] Index 'idx_cars_chat_id' checked/created.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] ERROR during database initialization: " + e.getMessage());
        } finally {
            closeConnection(conn, stmt, null);
            System.out.println("[DB] initializeDatabase finished.");
        }
    }

    // Сохранить новый автомобиль
    public long saveCar(Car car) {
        System.out.println("[DB] Attempting to save car for chat_id: " + car.getChatId() + ", make: " + car.getMake());
        String sql = "INSERT INTO cars (chat_id, \"make\", \"model\", \"year\", \"mileage\", \"price\", photo_file_id, \"last_maint_date\", \"last_maint_mileage\", \"last_maint_description\", \"last_maint_cost\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; // Добавлены 4 плейсхолдера
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        long carId = -1;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            stmt.setLong(1, car.getChatId());
            stmt.setString(2, car.getMake());
            stmt.setString(3, car.getModel());
            stmt.setInt(4, car.getYear());
            stmt.setInt(5, car.getMileage());
            stmt.setBigDecimal(6, car.getPrice());
            stmt.setString(7, car.getPhotoFileId());
            stmt.setDate(8, car.getLastMaintDate() != null ? java.sql.Date.valueOf(car.getLastMaintDate()) : null);
            stmt.setObject(9, car.getLastMaintMileage(), Types.INTEGER); // setObject для nullable int
            stmt.setString(10, car.getLastMaintDescription());
            stmt.setBigDecimal(11, car.getLastMaintCost());


            System.out.println("[DB] Executing INSERT for car...");
            int affectedRows = stmt.executeUpdate();
            System.out.println("[DB] INSERT affected rows: " + affectedRows);

            if (affectedRows == 0) {
                throw new SQLException("Создание авто не удалось, ни одна строка не затронута.");
            }
            generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                carId = generatedKeys.getLong(1);
                car.setId(carId);
                System.out.println("[DB] Generated car ID: " + carId);
            } else {
                throw new SQLException("Создание авто не удалось, не получен ID.");
            }

            System.out.println("[DB] Car saved successfully with ID: " + carId);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] ERROR saving car: " + e.getMessage());
        } finally {
            closeConnection(conn, stmt, generatedKeys);
            System.out.println("[DB] saveCar finished.");
        }
        return carId;
    }

    // Получить все автомобили пользователя
    public List<Car> getCarsByChatId(long chatId) {
        System.out.println("[DB] Attempting to get cars for chat_id: " + chatId);
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT id, chat_id, \"make\", \"model\", \"year\", \"mileage\", \"price\", photo_file_id, \"last_maint_date\", \"last_maint_mileage\", \"last_maint_description\", \"last_maint_cost\" FROM cars WHERE chat_id = ?"; // Добавлены 4 колонки
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, chatId);

            System.out.println("[DB] Executing SELECT * for chat_id: " + chatId);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Car car = new Car(
                        rs.getLong("id"),
                        rs.getLong("chat_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getInt("mileage"),
                        rs.getBigDecimal("price"),
                        rs.getString("photo_file_id"),
                        rs.getDate("last_maint_date") != null ? rs.getDate("last_maint_date").toLocalDate() : null,
                        rs.getObject("last_maint_mileage", Integer.class), // Считываем nullable int
                        rs.getString("last_maint_description"),
                        rs.getBigDecimal("last_maint_cost")
                );
                cars.add(car);
                System.out.println("[DB] Fetched car ID: " + car.getId() + ", make: " + car.getMake() + ", hasPhoto: " + (car.getPhotoFileId() != null && !car.getPhotoFileId().isEmpty()));
            }
            System.out.println("[DB] Found " + cars.size() + " cars for chat_id: " + chatId);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] ERROR getting cars for chat_id " + chatId + ": " + e.getMessage());
        } finally {
            closeConnection(conn, stmt, rs);
            System.out.println("[DB] getCarsByChatId finished.");
        }
        return cars;
    }

    // Получить конкретный автомобиль по ID
    public Car getCarById(long carId) {
        System.out.println("[DB] Attempting to get car by ID: " + carId);
        String sql = "SELECT id, chat_id, \"make\", \"model\", \"year\", \"mileage\", \"price\", photo_file_id, \"last_maint_date\", \"last_maint_mileage\", \"last_maint_description\", \"last_maint_cost\" FROM cars WHERE id = ?"; // Добавлены 4 колонки
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Car car = null;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, carId);

            System.out.println("[DB] Executing SELECT * for car ID: " + carId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                car = new Car(
                        rs.getLong("id"),
                        rs.getLong("chat_id"),
                        rs.getString("make"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getInt("mileage"),
                        rs.getBigDecimal("price"),
                        rs.getString("photo_file_id"),
                        rs.getDate("last_maint_date") != null ? rs.getDate("last_maint_date").toLocalDate() : null,
                        rs.getObject("last_maint_mileage", Integer.class), // Считываем nullable int
                        rs.getString("last_maint_description"),
                        rs.getBigDecimal("last_maint_cost")
                );
                System.out.println("[DB] Found car with ID: " + carId + ", make: " + car.getMake() + ", hasPhoto: " + (car.getPhotoFileId() != null && !car.getPhotoFileId().isEmpty()));
            } else {
                System.out.println("[DB] Car with ID " + carId + " not found.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] ERROR getting car by ID " + carId + ": " + e.getMessage());
        } finally {
            closeConnection(conn, stmt, rs);
            System.out.println("[DB] getCarById finished.");
        }
        return car;
    }


    // Обновить поле автомобиля по ID
    public boolean updateCarField(long carId, String fieldName, Object value) {
        System.out.println("[DB] Attempting to update car ID: " + carId + ", field: " + fieldName + ", value: " + value);
        String columnName = FIELD_TO_COLUMN_MAP.get(fieldName.toLowerCase());
        if (columnName == null) {
            System.err.println("[DB] ERROR: Attempt to update invalid or unmapped car field: " + fieldName);
            return false;
        }

        String sql = "UPDATE cars SET " + columnName + " = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            switch (fieldName.toLowerCase()) {
                case "make":
                case "model":
                case "photo_file_id":
                case "last_maint_description":
                    if (value == null || (value instanceof String && ((String)value).isEmpty())) {
                        stmt.setNull(1, Types.VARCHAR);
                    } else if (value instanceof String){
                        stmt.setString(1, (String) value);
                    } else {
                        System.err.println("[DB] ERROR: Invalid value type for String field " + fieldName + ": " + (value != null ? value.getClass().getName() : "null")); return false;
                    }
                    break;
                case "year":
                case "mileage":
                case "last_maint_mileage":
                    // Для целочисленных полей, включая nullable Integer
                    if (value == null) {
                        stmt.setNull(1, Types.INTEGER);
                    } else if (value instanceof Integer) {
                        stmt.setInt(1, (Integer) value);
                    } else {
                        System.err.println("[DB] ERROR: Invalid value type for Integer field " + fieldName + ": " + (value != null ? value.getClass().getName() : "null")); return false;
                    }
                    break;
                case "price":
                case "last_maint_cost":
                    // Для числовых полей BigDecimal, включая nullable
                    if (value == null) {
                        stmt.setNull(1, Types.DECIMAL);
                    } else if (value instanceof BigDecimal) {
                        stmt.setBigDecimal(1, (BigDecimal) value);
                    } else {
                        System.err.println("[DB] ERROR: Invalid value type for BigDecimal field " + fieldName + ": " + (value != null ? value.getClass().getName() : "null")); return false;
                    }
                    break;
                case "chat_id":
                    if (value instanceof Long) {
                        stmt.setLong(1, (Long) value);
                    } else {
                        System.err.println("[DB] ERROR: Invalid value type for Long field " + fieldName + ": " + (value != null ? value.getClass().getName() : "null")); return false;
                    }
                    break;
                case "last_maint_date":
                    // Для полей даты
                    if (value == null) {
                        stmt.setNull(1, Types.DATE);
                    } else if (value instanceof LocalDate) {
                        stmt.setDate(1, java.sql.Date.valueOf((LocalDate) value));
                    } else {
                        System.err.println("[DB] ERROR: Invalid value type for Date field " + fieldName + ": " + (value != null ? value.getClass().getName() : "null")); return false;
                    }
                    break;
                default:
                    System.err.println("[DB] ERROR: Attempt to update unsupported field: " + fieldName);
                    return false;
            }


            stmt.setLong(2, carId);

            System.out.println("[DB] Executing UPDATE for car ID: " + carId + ", column: " + columnName);
            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
            System.out.println("[DB] UPDATE affected rows: " + affectedRows + " for car ID: " + carId);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] ERROR updating car field " + fieldName + " (column " + columnName + ") for car ID " + carId + ": " + e.getMessage());
        } finally {
            closeConnection(conn, stmt, null);
            System.out.println("[DB] updateCarField finished for car ID: " + carId);
        }
        return success;
    }

    // Удалить автомобиль (без изменений)
    public boolean deleteCar(long carId) {
        System.out.println("[DB] Attempting to delete car ID: " + carId);
        String sql = "DELETE FROM cars WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setLong(1, carId);
            System.out.println("[DB] Executing DELETE for car ID: " + carId);
            int affectedRows = stmt.executeUpdate();
            success = affectedRows > 0;
            System.out.println("[DB] DELETE affected rows: " + affectedRows + " for car ID: " + carId);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("[DB] ERROR deleting car ID " + carId + ": " + e.getMessage());
        } finally {
            closeConnection(conn, stmt, null);
            System.out.println("[DB] deleteCar finished for car ID: " + carId);
        }
        return success;
    }

}