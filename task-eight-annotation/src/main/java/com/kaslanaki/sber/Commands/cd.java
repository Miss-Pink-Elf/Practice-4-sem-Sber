package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;
import com.kaslanaki.sber.CommandAnno;

import java.io.File;

@CommandAnno(name = "cd", description = "смена текущей рабочей директории")
public class cd implements Command {

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            System.out.println("Пожалуйста, укажите директорию");
            return;
        }
        String newDir = args[0];
        File dir = new File(newDir);
        if (dir.exists() && dir.isDirectory()) {
            System.setProperty("user.dir", dir.getAbsolutePath());
            System.out.println("Текущая директория изменена на: " + dir.getAbsolutePath());
        } else {
            System.out.println("Директория не найдена: " + newDir);
        }
    }
}


