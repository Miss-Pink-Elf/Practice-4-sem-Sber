package com.kaslanaki;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {

    public void addTask(String name) {
        String sql = "INSERT INTO tasks (name, status) VALUES (?, ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, "PENDING");
            pstmt.executeUpdate();
            System.out.println("Задача добавлена: \"" + name + "\"");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении задачи:");
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT id, name, status, created_at FROM tasks ORDER BY created_at DESC";
        try (Connection connection = DatabaseManager.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getTimestamp("created_at")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка задач:");
            e.printStackTrace();
        }
        return tasks;
    }

    public boolean markTaskAsDone(int taskId) {
        String checkSql = "SELECT status FROM tasks WHERE id = ?";
        String updateSql = "UPDATE tasks SET status = ? WHERE id = ? AND status = ?";

        try (Connection connection = DatabaseManager.getConnection()) {
            // Проверяем текущий статус задачи
            try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
                checkStmt.setInt(1, taskId);
                ResultSet rs = checkStmt.executeQuery();
                if (!rs.next()) {
                    System.out.println("Задача с ID " + taskId + " не найдена.");
                    return false;
                }
                if ("DONE".equals(rs.getString("status"))) {
                    System.out.println("Задача с ID " + taskId + " уже выполнена.");
                    return false;
                }
            }

            // Обновляем статус
            try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                updateStmt.setString(1, "DONE");
                updateStmt.setInt(2, taskId);
                updateStmt.setString(3, "PENDING");
                int affectedRows = updateStmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Задача с ID " + taskId + " отмечена как выполненная.");
                    return true;
                } else {
                    System.out.println("Не удалось обновить задачу с ID " + taskId + ". Возможно, она уже была выполнена или ID неверный.");
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении статуса задачи:");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Задача с ID " + taskId + " удалена.");
                return true;
            } else {
                System.out.println("Задача с ID " + taskId + " не найдена для удаления.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении задачи:");
            e.printStackTrace();
            return false;
        }
    }
}
