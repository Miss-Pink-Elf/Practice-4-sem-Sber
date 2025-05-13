package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;

import java.io.File;

public class ls implements Command {
    @Override
    public String getName() {
        return "ls";
    }

    public void execute(String[] args) {
        String currentDir = System.getProperty("user.dir");
        File dir = new File(currentDir);
        String[] files = dir.list();
        if (files != null) {
            for (String file : files) {
                System.out.println(file);
            }
        } else {
            System.out.println("Не удалось получить содержимое директории");
        }
    }
    @Override
    public String getHelp() {
        return "вывод содержимого теĸущей диреĸтории";
    }
}

