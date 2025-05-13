package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;
import com.kaslanaki.sber.CommandAnno;

@CommandAnno(name = "pwd", description = "вывод текущей рабочей директории")
public class pwd implements Command {

    @Override
    public void execute(String[] args) {
        String currentDir = System.getProperty("user.dir");
        System.out.println(currentDir);
    }
}

