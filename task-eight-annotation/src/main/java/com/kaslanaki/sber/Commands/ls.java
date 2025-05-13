package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;
import com.kaslanaki.sber.CommandAnno;

import java.io.File;

@CommandAnno(name = "ls", description = "вывод содержимого текущей директории")
public class ls implements Command {

    @Override
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
}

