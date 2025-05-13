package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;

public class exit implements Command {

    @Override
    public String getName() {
        return "exit";
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Выход из приложения...");
        System.exit(0);
    }

    @Override
    public String getHelp() {
        return "завершение работы приложения";
    }
}