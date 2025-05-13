package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;
import com.kaslanaki.sber.CommandAnno;

@CommandAnno(name = "exit", description = "выход из Shell")
public class exit implements Command {
    @Override
    public void execute(String[] args) {
        System.out.println("Выход из приложения...");
        System.exit(0);
    }
}