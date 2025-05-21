package com.kaslanaki;

public enum BotState {
    IDLE, // Бот ждет команды

    AWAITING_MAKE, // Ожидает марку
    AWAITING_MODEL, // Ожидает модель
    AWAITING_YEAR, // Ожидает год
    AWAITING_MILEAGE, // Ожидает пробег
    AWAITING_PRICE, // Ожидает стоимость
    AWAITING_PHOTO, // Ожидает фото
    EDITING_SELECT_FIELD, // Ожидает выбор поля для редактирования
    EDITING_MAKE, // Редактирование марки
    EDITING_MODEL, // Редактирование модели
    EDITING_YEAR, // Редактирование года
    EDITING_MILEAGE, // Редактирование пробега (текущего)
    EDITING_PRICE, // Редактирование стоимости
    EDITING_PHOTO, // Редактирование фото
    EDITING_LAST_MAINT_DATE, // Редактирование даты последнего ТО
    EDITING_LAST_MAINT_MILEAGE, // Редактирование пробега последнего ТО
    EDITING_LAST_MAINT_DESCRIPTION, // Редактирование описания последнего ТО
    EDITING_LAST_MAINT_COST, // Редактирование стоимости последнего ТО

    CONFIRM_DELETE_CAR, // Ожидает подтверждение удаления автомобиля

}