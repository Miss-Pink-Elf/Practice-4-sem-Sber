package com.kaslanaki;

import java.sql.Timestamp;

public class Task {
    private int id;         // UID
    private String name;    // Name
    private String status;  // Status
    private Timestamp createdAt;

    public Task(int id, String name, String status, Timestamp createdAt) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return String.format("ID: %d, Задача: \"%s\", Статус: %s (Создана: %s)",
                id, name, status, createdAt.toLocalDateTime().toLocalDate());
    }
}