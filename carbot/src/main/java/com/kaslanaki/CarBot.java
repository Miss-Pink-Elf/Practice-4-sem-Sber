package com.kaslanaki;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.kaslanaki.BotState.*;

public class CarBot extends TelegramLongPollingBot {

    private final String BOT_USERNAME = "itsaCarBot";
    private final String BOT_TOKEN = "7793230616:AAHoXaJaqgjNS3LCH_A_rtOEDm-heLLhrF4";

    private final DatabaseManager databaseManager;
    private final Map<Long, UserState> userStates;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public CarBot() {
        this.databaseManager = new DatabaseManager();
        this.userStates = new ConcurrentHashMap<>();
        System.out.println("CarBot экземпляр создан. Username: " + BOT_USERNAME);
        if (BOT_TOKEN.equals("BOT_TOKEN") || BOT_USERNAME.equals("BOT_USERNAME")) {
            System.err.println("Неверно указаны токен или/и юзернейм");
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void clearWebhook() {
        try {
            super.clearWebhook();
            System.out.println("INFO: Successfully cleared any existing webhook or no webhook was present.");
        } catch (TelegramApiRequestException e) {
            if (e.getErrorCode() != null && e.getErrorCode() == 404) {
                System.out.println("INFO: No webhook was set or it was already removed (received 404 on deleteWebhook). " +
                        "This is normal for long polling. Bot will proceed. Original error: " + e.getMessage());
            } else {
                System.err.println("Error during clearWebhook that is not a 404: " + e.getMessage());
            }
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        long chatId = 0;

        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
        } else {
            return;
        }

        UserState currentUserState = userStates.computeIfAbsent(chatId, k -> new UserState());

        if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            deleteMessage(chatId, messageId);

            handleCallbackQuery(chatId, callbackData, currentUserState);
            return;
        }

        // (Commands, Photo, Text)
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                if (handleCommand(chatId, messageText, currentUserState)) {
                    return;
                }
            }

            // Photo Uploads
            if (update.getMessage().hasPhoto()) {
                if (currentUserState.getCurrentState() == AWAITING_PHOTO || currentUserState.getCurrentState() == EDITING_PHOTO) {
                    handlePhotoUpload(chatId, update.getMessage().getPhoto(), currentUserState);
                    return;
                } else {
                    sendMessage(chatId, "Фотография была отправлена не вовремя. Пожалуйста, следуйте инструкциям.");
                    sendCurrentStepPrompt(chatId, currentUserState);
                    return;
                }
            }

            if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                if (!isCommand(messageText)) {
                    handleStatefulTextInput(chatId, messageText, currentUserState);
                    return;
                }
            }
        }

        if (currentUserState.getCurrentState() != IDLE) {
            sendCurrentStepPrompt(chatId, currentUserState);
        }
    }

    private boolean isCommand(String text) {
        return text.equals("/start") || text.equals("Добавить авто") || text.equals("Мои авто");
    }

    private boolean handleCommand(long chatId, String messageText, UserState currentUserState) {
        if (messageText.equals("/start")) {
            currentUserState.resetState();
            sendWelcomeMessage(chatId);
            sendMainMenu(chatId);
            return true;
        }
        if (currentUserState.getCurrentState() == IDLE) {
            if (messageText.equals("Добавить авто")) {
                currentUserState.setCurrentState(AWAITING_MAKE);
                currentUserState.setTempCar(new Car(chatId));
                sendMakeQuestion(chatId, "Выберите марку автомобиля или введите ее текстом:");
                return true;
            } else if (messageText.equals("Мои авто")) {
                currentUserState.resetState();
                showUserCars(chatId);
                return true;
            }
        }
        return false;
    }

    private void handleCallbackQuery(long chatId, String callbackData, UserState currentUserState) {
        if (callbackData.startsWith("make:") && currentUserState.getCurrentState() == AWAITING_MAKE) {
            String make = callbackData.substring("make:".length());
            if (currentUserState.getTempCar() == null) { handleStateError(chatId, currentUserState, "Ошибка: временные данные авто не найдены. Начните добавление заново."); return; }
            if (!make.equals("Другая марка")) {
                currentUserState.getTempCar().setMake(make);
                currentUserState.setCurrentState(AWAITING_MODEL);
                sendMessage(chatId, "Отлично! Теперь введите модель вашего " + make + ":");
            } else {
                sendMessage(chatId, "Хорошо, введите марку вашего автомобиля:");
            }
        }
        // Обработка действий с конкретным авто (редактировать/удалить)
        else if (callbackData.startsWith("car_action:") && currentUserState.getCurrentState() == IDLE) {
            String[] parts = callbackData.split(":");
            if (parts.length == 3) {
                try {
                    long carId = Long.parseLong(parts[1]);
                    String action = parts[2];
                    Car car = databaseManager.getCarById(carId);
                    if (car != null && car.getChatId() == chatId) {
                        currentUserState.setAssociatedId(carId);
                        switch (action) {
                            case "edit": // Кнопка редактирования
                                currentUserState.setCurrentState(EDITING_SELECT_FIELD);
                                sendEditOptions(chatId, car);
                                break;
                            case "delete": // Кнопка удаления
                                currentUserState.setCurrentState(CONFIRM_DELETE_CAR);
                                sendConfirmationDelete(chatId, car);
                                break;
                            default:
                                sendMessage(chatId, "Неизвестное действие с автомобилем.");
                                currentUserState.resetState(); sendMainMenu(chatId);
                                break;
                        }
                    } else {
                        sendMessage(chatId, "Извините, не удалось найти или получить доступ к этому авто.");
                        currentUserState.resetState(); sendMainMenu(chatId);
                    }
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Ошибка: неверный ID автомобиля в callback.");
                    currentUserState.resetState();
                    sendMainMenu(chatId);
                }
            }
        }
        else if (callbackData.startsWith("edit_field:") && currentUserState.getCurrentState() == EDITING_SELECT_FIELD) {
            String[] parts = callbackData.split(":");
            if (parts.length == 3) {
                try {
                    long carIdFromCallback = Long.parseLong(parts[1]);
                    String fieldName = parts[2];
                    if (currentUserState.getAssociatedId() != carIdFromCallback) {
                        handleStateError(chatId, currentUserState, "Ошибка: ID редактируемого авто не совпадает. Пожалуйста, попробуйте снова.");
                        return;
                    }
                    Car carToEdit = databaseManager.getCarById(currentUserState.getAssociatedId());
                    if (carToEdit != null) {
                        switch (fieldName) {
                            case "make":
                                currentUserState.setCurrentState(EDITING_MAKE);
                                sendMessage(chatId, "Введите новую марку:");
                                break;
                            case "model":
                                currentUserState.setCurrentState(EDITING_MODEL);
                                sendMessage(chatId, "Введите новую модель:");
                                break;
                            case "year":
                                currentUserState.setCurrentState(EDITING_YEAR);
                                sendMessage(chatId, "Введите новый год производства:");
                                break;
                            case "mileage":
                                currentUserState.setCurrentState(EDITING_MILEAGE);
                                sendMessage(chatId, "Введите новый текущий пробег (км):");
                                break;
                            case "price":
                                currentUserState.setCurrentState(EDITING_PRICE);
                                sendMessage(chatId, "Введите новую стоимость:");
                                break;
                            case "photo":
                                currentUserState.setCurrentState(EDITING_PHOTO);
                                sendMessage(chatId, "Пришлите новое фото автомобиля:");
                                break;
                            case "last_maint_date":
                                currentUserState.setCurrentState(EDITING_LAST_MAINT_DATE);
                                sendMessage(chatId, "Введите дату последнего ТО в формате ДД.ММ.ГГГГ (или \"-\" чтобы очистить):");
                                break;
                            case "last_maint_mileage":
                                currentUserState.setCurrentState(EDITING_LAST_MAINT_MILEAGE);
                                sendMessage(chatId, "Введите пробег (в км) на момент последнего ТО (или \"-\" чтобы очистить):");
                                break;
                            case "last_maint_description":
                                currentUserState.setCurrentState(EDITING_LAST_MAINT_DESCRIPTION);
                                sendMessage(chatId, "Введите описание работ по последнему ТО (или \"-\" чтобы очистить):");
                                break;
                            case "last_maint_cost":
                                currentUserState.setCurrentState(EDITING_LAST_MAINT_COST);
                                sendMessage(chatId, "Введите стоимость последнего ТО (или \"-\" чтобы очистить):");
                                break;
                            default:
                                sendMessage(chatId, "Извините, это поле нельзя редактировать.");
                                currentUserState.resetState();
                                sendMainMenu(chatId);
                                break;
                        }
                    } else {
                        handleStateError(chatId, currentUserState, "Ошибка: Авто не найдено для редактирования.");
                    }
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Ошибка: неверный ID автомобиля в callback для редактирования поля.");
                    currentUserState.resetState();
                    sendMainMenu(chatId);
                }
            } else {
                sendMessage(chatId, "Ошибка в формате callback для редактирования поля.");
                currentUserState.resetState(); sendMainMenu(chatId);
            }
        }
        // Обработка подтверждения удаления
        else if (callbackData.startsWith("confirm_delete:") && currentUserState.getCurrentState() == CONFIRM_DELETE_CAR) {
            String[] parts = callbackData.split(":");
            if (parts.length == 3) {
                try {
                    long carIdFromCallback = Long.parseLong(parts[1]);
                    String choice = parts[2];
                    if (currentUserState.getAssociatedId() == carIdFromCallback) {
                        if (choice.equals("yes")) {
                            boolean deleted = databaseManager.deleteCar(currentUserState.getAssociatedId());
                            sendMessage(chatId, deleted ? "Автомобиль удален." : "Ошибка при удалении автомобиля.");
                        } else {
                            sendMessage(chatId, "Удаление отменено.");
                        }
                    } else {
                        sendMessage(chatId, "Ошибка: ID автомобиля для удаления не совпадает. Попробуйте снова.");
                    }
                    currentUserState.resetState();
                    sendMainMenu(chatId);
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Ошибка: неверный ID автомобиля в callback для подтверждения удаления.");
                    currentUserState.resetState();
                    sendMainMenu(chatId);
                }
            } else {
                sendMessage(chatId, "Ошибка в формате callback для подтверждения удаления.");
                currentUserState.resetState();
                sendMainMenu(chatId);
            }
        }
        else {
            sendCurrentStepPrompt(chatId, currentUserState);
        }
    }

    private void handlePhotoUpload(long chatId, List<PhotoSize> photos, UserState currentUserState) {
        if (photos == null || photos.isEmpty()) {
            sendMessage(chatId, "Не удалось получить фото. Попробуйте еще раз.");
            return;
        }
        String fileId = photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).map(PhotoSize::getFileId).orElse(null);

        if (fileId == null) {
            sendMessage(chatId, "Не удалось обработать ID фото. Попробуйте еще раз.");
            return;
        }

        if (currentUserState.getCurrentState() == AWAITING_PHOTO) {
            Car carToSave = currentUserState.getTempCar();
            if (carToSave != null) {
                carToSave.setPhotoFileId(fileId);
                long savedCarId = databaseManager.saveCar(carToSave);
                sendMessage(chatId, savedCarId > 0 ? "Отлично! Ваш автомобиль сохранен. Информацию о последнем ТО можно добавить позже через редактирование." : "Произошла ошибка при сохранении автомобиля.");
            } else {
                handleStateError(chatId, currentUserState, "Ошибка: Временные данные для сохранения авто не найдены."); return;
            }
        } else if (currentUserState.getCurrentState() == EDITING_PHOTO) {
            long carIdToEditPhoto = currentUserState.getAssociatedId();
            if (carIdToEditPhoto > 0) {
                boolean updated = databaseManager.updateCarField(carIdToEditPhoto, "photo_file_id", fileId);
                sendMessage(chatId, updated ? "Фото обновлено." : "Ошибка при обновлении фото.");
            } else {
                handleStateError(chatId, currentUserState, "Ошибка: Не указан ID автомобиля для обновления фото."); return;
            }
        }
        currentUserState.resetState();
        sendMainMenu(chatId);
    }

    private void handleStatefulTextInput(long chatId, String messageText, UserState currentUserState) {
        Car tempCar = currentUserState.getTempCar(); // Для нового авто
        long associatedId = currentUserState.getAssociatedId(); // Для редактирования авто

        boolean resetAfterHandling = false;
        String successMessage = null;

        switch (currentUserState.getCurrentState()) {
            case AWAITING_MAKE:
                if (tempCar == null) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                tempCar.setMake(messageText);
                currentUserState.setCurrentState(AWAITING_MODEL);
                sendMessage(chatId, "Отлично! Теперь введите модель вашего " + messageText + ":");
                break;
            case AWAITING_MODEL:
                if (tempCar == null) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                tempCar.setModel(messageText);
                currentUserState.setCurrentState(AWAITING_YEAR);
                sendMessage(chatId, "Введите год производства:");
                break;
            case AWAITING_YEAR:
                if (tempCar == null) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    int year = Integer.parseInt(messageText);
                    if (year < 1800 || year > java.time.Year.now().getValue() + 1) {
                        sendMessage(chatId, "Пожалуйста, введите корректный год (например, 2020).");
                        return;
                    }
                    tempCar.setYear(year);
                    currentUserState.setCurrentState(AWAITING_MILEAGE);
                    sendMessage(chatId, "Введите текущий пробег (км):");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Пожалуйста, введите год цифрами. Например: 2020");
                }
                break;
            case AWAITING_MILEAGE:
                if (tempCar == null) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    int mileage = Integer.parseInt(messageText);
                    if (mileage < 0) {
                        sendMessage(chatId, "Пробег не может быть отрицательным.");
                        return;
                    }
                    tempCar.setMileage(mileage);
                    currentUserState.setCurrentState(AWAITING_PRICE);
                    sendMessage(chatId, "Введите стоимость:");
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Пожалуйста, введите пробег цифрами. Например: 50000");
                }
                break;
            case AWAITING_PRICE:
                if (tempCar == null) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    BigDecimal price = new BigDecimal(messageText.replace(",", "."));
                    if (price.compareTo(BigDecimal.ZERO) < 0) {
                        sendMessage(chatId, "Стоимость не может быть отрицательной.");
                        return;
                    }
                    tempCar.setPrice(price);
                    currentUserState.setCurrentState(AWAITING_PHOTO);
                    sendMessage(chatId, "Почти готово! Теперь пришлите фото автомобиля или пропустите этот шаг."); // Теперь можно пропустить фото
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Пожалуйста, введите стоимость цифрами. Например: 15000.50");
                }
                break;

            case EDITING_MAKE:
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                databaseManager.updateCarField(associatedId, "make", messageText);
                successMessage = "Марка обновлена.";
                resetAfterHandling = true;
                break;
            case EDITING_MODEL:
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                databaseManager.updateCarField(associatedId, "model", messageText);
                successMessage = "Модель обновлена.";
                resetAfterHandling = true;
                break;
            case EDITING_YEAR:
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    int year = Integer.parseInt(messageText);
                    if (year < 1800 || year > java.time.Year.now().getValue() + 1) {
                        sendMessage(chatId, "Пожалуйста, введите корректный год (например, 2020).");
                        return;
                    }
                    databaseManager.updateCarField(associatedId, "year", year);
                    successMessage = "Год обновлен.";
                    resetAfterHandling = true;
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Пожалуйста, введите год цифрами.");
                }
                break;
            case EDITING_MILEAGE: // Редактирование текущего пробега
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    int mileage = Integer.parseInt(messageText);
                    if (mileage < 0) {
                        sendMessage(chatId, "Пробег не может быть отрицательным.");
                        return;
                    }
                    databaseManager.updateCarField(associatedId, "mileage", mileage);
                    successMessage = "Пробег обновлен.";
                    resetAfterHandling = true;
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Пожалуйста, введите пробег цифрами.");
                }
                break;
            case EDITING_PRICE:
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    BigDecimal price = new BigDecimal(messageText.replace(",", "."));
                    if (price.compareTo(BigDecimal.ZERO) < 0) {
                        sendMessage(chatId, "Стоимость не может быть отрицательной.");
                        return;
                    }
                    databaseManager.updateCarField(associatedId, "price", price);
                    successMessage = "Стоимость обновлена.";
                    resetAfterHandling = true;
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Пожалуйста, введите стоимость цифрами.");
                }
                break;
            case EDITING_LAST_MAINT_DATE:
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    LocalDate serviceDate = null;
                    if (!messageText.trim().equals("-")) { // Если пользователь не ввел "-", пытаемся парсить дату
                        serviceDate = LocalDate.parse(messageText, DATE_FORMATTER);
                    }
                    databaseManager.updateCarField(associatedId, "last_maint_date", serviceDate);
                    successMessage = (serviceDate == null ? "Дата последнего ТО очищена." : "Дата последнего ТО обновлена.");
                    resetAfterHandling = true;
                } catch (DateTimeParseException e) {
                    sendMessage(chatId, "Неверный формат даты. Пожалуйста, введите дату в формате ДД.ММ.ГГГГ или \"-\" для очистки.");
                }
                break;
            case EDITING_LAST_MAINT_MILEAGE:
                if (associatedId == 0) { handleStateError(chatId, currentUserState); return; }
                try {
                    Integer mileage = null;
                    if (!messageText.trim().equals("-")) { // Если пользователь не ввел "-", пытаемся парсить число
                        mileage = Integer.parseInt(messageText);
                        if (mileage < 0) {
                            sendMessage(chatId, "Пробег не может быть отрицательным.");
                            return;
                        }
                    }
                    databaseManager.updateCarField(associatedId, "last_maint_mileage", mileage);
                    successMessage = (mileage == null ? "Пробег последнего ТО очищен." : "Пробег последнего ТО обновлен.");
                    resetAfterHandling = true;
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Неверный формат пробега. Пожалуйста, введите пробег цифрами или \"-\" для очистки.");
                }
                break;
            case EDITING_LAST_MAINT_DESCRIPTION:
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                String description = messageText.trim().equals("-") ? null : messageText; // Очищаем, если ввели "-"
                databaseManager.updateCarField(associatedId, "last_maint_description", description);
                successMessage = (description == null ? "Описание последнего ТО очищено." : "Описание последнего ТО обновлено.");
                resetAfterHandling = true;
                break;
            case EDITING_LAST_MAINT_COST:
                if (associatedId == 0) {
                    handleStateError(chatId, currentUserState);
                    return;
                }
                try {
                    BigDecimal cost = null;
                    if (!messageText.trim().equals("-")) {
                        cost = new BigDecimal(messageText.replace(",", "."));
                        if (cost.compareTo(BigDecimal.ZERO) < 0) {
                            sendMessage(chatId, "Стоимость не может быть отрицательной.");
                            return;
                        }
                    }
                    databaseManager.updateCarField(associatedId, "last_maint_cost", cost);
                    successMessage = (cost == null ? "Стоимость последнего ТО очищена." : "Стоимость последнего ТО обновлена.");
                    resetAfterHandling = true;
                } catch (NumberFormatException e) {
                    sendMessage(chatId, "Неверный формат стоимости. Пожалуйста, введите стоимость цифрами (например, 5500.75) или \"-\" для очистки.");
                }
                break;
            case IDLE:
                sendMessage(chatId, "Я не знаю такой команды. Выберите действие из меню или используйте /start.");
                break;
            default:
                sendCurrentStepPrompt(chatId, currentUserState);
                break;
        }

        if (resetAfterHandling) {
            if (successMessage != null) {
                sendMessage(chatId, successMessage);
            }
            currentUserState.resetState();
            sendMainMenu(chatId);
        }
    }


    private void sendCurrentStepPrompt(long chatId, UserState currentUserState) {
        String prompt = "Пожалуйста, ";
        boolean specificPromptSent = false;

        switch (currentUserState.getCurrentState()) {
            case AWAITING_MAKE: sendMakeQuestion(chatId, "Выберите марку или введите ее текстом:"); specificPromptSent = true; break; // Можно убрать клавиатуру, если не нужна
            case AWAITING_MODEL: prompt += "введите модель:"; break;
            case AWAITING_YEAR: prompt += "введите год производства цифрами:"; break;
            case AWAITING_MILEAGE: prompt += "введите текущий пробег (км) цифрами:"; break;
            case AWAITING_PRICE: prompt += "введите стоимость цифрами:"; break;
            case AWAITING_PHOTO: prompt += "пришлите фото автомобиля или пропустите этот шаг:"; break;

            case EDITING_SELECT_FIELD:
                Car carForEditPrompt = databaseManager.getCarById(currentUserState.getAssociatedId());
                if (carForEditPrompt != null) sendEditOptions(chatId, carForEditPrompt);
                else handleStateError(chatId, currentUserState, "Ошибка: авто для редактирования не найдено.");
                specificPromptSent = true; break;
            case EDITING_MAKE: prompt += "введите новую марку:"; break;
            case EDITING_MODEL: prompt += "введите новую модель:"; break;
            case EDITING_YEAR: prompt += "введите новый год цифрами:"; break;
            case EDITING_MILEAGE: prompt += "введите новый текущий пробег (км) цифрами:"; break;
            case EDITING_PRICE: prompt += "введите новую стоимость цифрами:"; break;
            case EDITING_PHOTO: prompt += "пришлите новое фото автомобиля:"; break;
            case EDITING_LAST_MAINT_DATE: prompt += "введите дату последнего ТО в формате ДД.ММ.ГГГГ или \"-\" для очистки:"; break;
            case EDITING_LAST_MAINT_MILEAGE: prompt += "введите пробег (в км) на момент последнего ТО цифрами или \"-\" для очистки:"; break;
            case EDITING_LAST_MAINT_DESCRIPTION: prompt += "введите описание работ по последнему ТО или \"-\" для очистки:"; break;
            case EDITING_LAST_MAINT_COST: prompt += "введите стоимость последнего ТО цифрами или \"-\" для очистки:"; break;
            case CONFIRM_DELETE_CAR:
                Car carForDeletePrompt = databaseManager.getCarById(currentUserState.getAssociatedId());
                if (carForDeletePrompt != null) sendConfirmationDelete(chatId, carForDeletePrompt);
                else handleStateError(chatId, currentUserState, "Ошибка: авто для удаления не найдено.");
                specificPromptSent = true; break;
            case IDLE:
                prompt = "Выберите действие из меню:";
                sendMainMenu(chatId);
                specificPromptSent = true;
                break;
            default: prompt = "Я не совсем понял. Пожалуйста, следуйте инструкциям или используйте /start."; break;
        }

        if (!specificPromptSent && !prompt.equals("Пожалуйста, ") && !prompt.isEmpty()) {
            sendMessage(chatId, prompt);
        }
    }

    private void handleStateError(long chatId, UserState state) {
        handleStateError(chatId, state, "Произошла внутренняя ошибка состояния. Пожалуйста, начните заново с /start.");
    }
    private void handleStateError(long chatId, UserState state, String errorMessage) {
        sendMessage(chatId, errorMessage);
        if (state != null) state.resetState();
        sendMainMenu(chatId);
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.err.println("SendMessage Error: " + e.getMessage());
        }
    }

    private void sendPhotoMessage(long chatId, String photoFileId, String caption) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile(photoFileId));
        sendPhoto.setCaption(caption);
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            System.err.println("SendPhoto Error: " + e.getMessage());
        }
    }

    private void deleteMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(String.valueOf(chatId));
        deleteMessage.setMessageId(messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
        }
    }

    private void sendWelcomeMessage(long chatId) {
        String welcomeText = "Привет! Я бот для учета твоих автомобилей.\n\n" +
                "Ты можешь добавить информацию о своих машинах, просмотреть их, а также редактировать или удалять данные, включая информацию о последнем ТО.\n\n" +
                "Используй кнопки ниже, чтобы начать.";
        sendMessage(chatId, welcomeText);
    }

    private void sendMainMenu(long chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add("Добавить авто");
        keyboard.add(row1);
        KeyboardRow row2 = new KeyboardRow();
        row2.add("Мои авто");
        keyboard.add(row2);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите действие:");
        message.setReplyMarkup(keyboardMarkup);
        try {
            execute(message);
        } catch (TelegramApiException e) { System.err.println("SendMainMenu Error: " + e.getMessage()); }
    }

    private void sendMakeQuestion(long chatId, String questionText) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(questionText);
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row1Buttons = new ArrayList<>();
        row1Buttons.add(createInlineButton("Lada", "make:Lada"));
        row1Buttons.add(createInlineButton("BMW", "make:BMW"));
        rowsInline.add(row1Buttons);
        List<InlineKeyboardButton> row2Buttons = new ArrayList<>();
        row2Buttons.add(createInlineButton("Audi", "make:Audi"));
        row2Buttons.add(createInlineButton("Opel", "make:Opel"));
        rowsInline.add(row2Buttons);
        List<InlineKeyboardButton> row3Buttons = new ArrayList<>();
        row3Buttons.add(createInlineButton("Другая марка", "make:Другая марка"));
        rowsInline.add(row3Buttons);
        inlineKeyboard.setKeyboard(rowsInline);
        message.setReplyMarkup(inlineKeyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) { System.err.println("SendMakeQuestion Error: " + e.getMessage());}
    }

    private InlineKeyboardButton createInlineButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);
        return button;
    }

    private void showUserCars(long chatId) {
        List<Car> cars = databaseManager.getCarsByChatId(chatId);
        if (cars.isEmpty()) {
            sendMessage(chatId, "У вас пока нет сохраненных автомобилей.");
            sendMainMenu(chatId);
            return;
        }
        sendMessage(chatId, "Ваши автомобили:");
        for (Car car : cars) {
            String caption = car.toString();
            if (car.getPhotoFileId() != null && !car.getPhotoFileId().isEmpty()) {
                sendPhotoWithCarActions(chatId, car.getPhotoFileId(), caption, car.getId());
            } else {
                sendMessageWithCarActions(chatId, caption, car.getId());
            }
        }
    }

    // Методы отправки сообщений/фото с клавиатурой действий
    private void sendMessageWithCarActions(long chatId, String text, long carId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setReplyMarkup(createCarActionKeyboard(carId));
        try {
            execute(message);
        } catch (TelegramApiException e) { System.err.println("SendMessageWithCarActions Error: " + e.getMessage()); }
    }

    private void sendPhotoWithCarActions(long chatId, String photoFileId, String caption, long carId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile(photoFileId));
        sendPhoto.setCaption(caption);
        sendPhoto.setReplyMarkup(createCarActionKeyboard(carId));
        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) { System.err.println("SendPhotoWithCarActions Error: " + e.getMessage()); }
    }

    // Клавиатура действий для конкретного авто
    private InlineKeyboardMarkup createCarActionKeyboard(long carId) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(createInlineButton("Редактировать", "car_action:" + carId + ":edit"));
        row1.add(createInlineButton("Удалить", "car_action:" + carId + ":delete"));
        rowsInline.add(row1);

        inlineKeyboard.setKeyboard(rowsInline);
        return inlineKeyboard;
    }

    // Меню выбора поля для редактирования
    private void sendEditOptions(long chatId, Car car) {
        if (car == null) {
            handleStateError(chatId, userStates.get(chatId), "Ошибка: Авто для редактирования не найдено.");
            return;
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Выберите, что хотите отредактировать для:\n" + car.getMake() + " " + car.getModel());
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();
        row1.add(createInlineButton("Марка", "edit_field:" + car.getId() + ":make"));
        row1.add(createInlineButton("Модель", "edit_field:" + car.getId() + ":model"));
        rowsInline.add(row1);
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        row2.add(createInlineButton("Год", "edit_field:" + car.getId() + ":year"));
        row2.add(createInlineButton("Пробег (текущий)", "edit_field:" + car.getId() + ":mileage")); // Уточнение
        rowsInline.add(row2);
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        row3.add(createInlineButton("Стоимость", "edit_field:" + car.getId() + ":price"));
        row3.add(createInlineButton("Фото", "edit_field:" + car.getId() + ":photo"));
        rowsInline.add(row3);
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        row4.add(createInlineButton("ТО: Дата", "edit_field:" + car.getId() + ":last_maint_date"));
        row4.add(createInlineButton("ТО: Пробег", "edit_field:" + car.getId() + ":last_maint_mileage"));
        rowsInline.add(row4);
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        row5.add(createInlineButton("ТО: Описание", "edit_field:" + car.getId() + ":last_maint_description"));
        row5.add(createInlineButton("ТО: Стоимость", "edit_field:" + car.getId() + ":last_maint_cost"));
        rowsInline.add(row5);

        inlineKeyboard.setKeyboard(rowsInline);
        message.setReplyMarkup(inlineKeyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) { System.err.println("SendEditOptions Error: " + e.getMessage()); }
    }

    private void sendConfirmationDelete(long chatId, Car car) {
        if (car == null) {
            handleStateError(chatId, userStates.get(chatId), "Ошибка: Авто для удаления не найдено.");
            return;
        }
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Вы уверены, что хотите удалить автомобиль:\n" + car.getMake() + " " + car.getModel() + "?");
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        row.add(createInlineButton("Да", "confirm_delete:" + car.getId() + ":yes"));
        row.add(createInlineButton("Нет", "confirm_delete:" + car.getId() + ":no"));
        rowsInline.add(row);
        inlineKeyboard.setKeyboard(rowsInline);
        message.setReplyMarkup(inlineKeyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) { System.err.println("SendConfirmationDelete Error: " + e.getMessage()); }
    }

}