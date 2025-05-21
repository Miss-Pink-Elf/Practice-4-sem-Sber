package com.kaslanaki;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Car {
    private long id; // ID в базе данных
    private long chatId; // ID пользователя Telegram
    private String make;
    private String model;
    private int year;
    private int mileage; // Текущий пробег
    private BigDecimal price;
    private String photoFileId; // Telegram File ID для фото
    private LocalDate lastMaintDate; // Дата последнего ТО
    private Integer lastMaintMileage; // Пробег на момент последнего ТО (Integer для nullable)
    private String lastMaintDescription; // Описание последнего ТО
    private BigDecimal lastMaintCost; // Стоимость последнего ТО

    public Car(long chatId) {
        this.chatId = chatId;
    }

    // Конструктор для загрузки из базы
    public Car(long id, long chatId, String make, String model, int year, int mileage, BigDecimal price, String photoFileId,
               LocalDate lastMaintDate, Integer lastMaintMileage, String lastMaintDescription, BigDecimal lastMaintCost) {
        this.id = id;
        this.chatId = chatId;
        this.make = make;
        this.model = model;
        this.year = year;
        this.mileage = mileage;
        this.price = price;
        this.photoFileId = photoFileId;
        this.lastMaintDate = lastMaintDate;
        this.lastMaintMileage = lastMaintMileage;
        this.lastMaintDescription = lastMaintDescription;
        this.lastMaintCost = lastMaintCost;
    }

    // Getters and Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getChatId() { return chatId; }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getPhotoFileId() { return photoFileId; }
    public void setPhotoFileId(String photoFileId) { this.photoFileId = photoFileId; }

    public LocalDate getLastMaintDate() { return lastMaintDate; }
    public void setLastMaintDate(LocalDate lastMaintDate) { this.lastMaintDate = lastMaintDate; }

    public Integer getLastMaintMileage() { return lastMaintMileage; }
    public void setLastMaintMileage(Integer lastMaintMileage) { this.lastMaintMileage = lastMaintMileage; }

    public String getLastMaintDescription() { return lastMaintDescription; }
    public void setLastMaintDescription(String lastMaintDescription) { this.lastMaintDescription = lastMaintDescription; }

    public BigDecimal getLastMaintCost() { return lastMaintCost; }
    public void setLastMaintCost(BigDecimal lastMaintCost) { this.lastMaintCost = lastMaintCost; }


    @Override
    public String toString() {
        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        StringBuilder sb = new StringBuilder();
        sb.append("Марка: ").append(make).append("\n");
        sb.append("Модель: ").append(model).append("\n");
        sb.append("Год: ").append(year).append("\n");
        sb.append("Пробег: ").append(mileage).append(" км\n");
        sb.append("Цена: ").append(price != null ? price.toPlainString() : "не указана").append("\n");
        sb.append("--------------------\n");
        sb.append("Последнее ТО:\n");
        if (lastMaintDate != null) {
            sb.append("  Дата: ").append(lastMaintDate.format(displayFormatter)).append("\n");
        }
        if (lastMaintMileage != null) {
            sb.append("  Пробег: ").append(lastMaintMileage).append(" км\n");
        }
        if (lastMaintDescription != null && !lastMaintDescription.isEmpty()) {
            sb.append("  Описание: ").append(lastMaintDescription).append("\n");
        }
        if (lastMaintCost != null) {
            sb.append("  Стоимость: ").append(lastMaintCost.toPlainString()).append("\n");
        }
        if (lastMaintDate == null && lastMaintMileage == null && lastMaintDescription == null && lastMaintCost == null) {
            sb.append("  нет данных\n");
        }
        return sb.toString();
    }
}