package com.kaslanaki;

public class UserState {
    private BotState currentState;
    private Car tempCar; // Для нового авто
    private long associatedId; // Используется для ID авто при редактировании

    public UserState() {
        this.currentState = BotState.IDLE;
    }

    public BotState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(BotState currentState) {
        this.currentState = currentState;
    }

    public Car getTempCar() {
        return tempCar;
    }

    public void setTempCar(Car tempCar) {
        this.tempCar = tempCar;
    }

    public long getAssociatedId() {
        return associatedId;
    }

    public void setAssociatedId(long associatedId) {
        this.associatedId = associatedId;
    }

    // методы для очистки/сброса состояния
    public void resetState() {
        this.currentState = BotState.IDLE;
        this.tempCar = null;
        this.associatedId = 0;
    }
}