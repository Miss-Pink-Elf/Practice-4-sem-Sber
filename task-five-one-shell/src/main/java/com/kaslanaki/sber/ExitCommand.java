package com.kaslanaki.sber;

// Команда выхода из приложения
public class ExitCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Выход из приложения...");
        System.exit(0);
    }

    @Override
    public String getDescription() {
        return "exit - завершает работу приложения";
    }
}
