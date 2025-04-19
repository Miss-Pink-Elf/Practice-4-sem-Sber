package com.kaslanaki.sber;

import java.text.SimpleDateFormat;
import java.util.Date;

// Команда для вывода текущего времени
public class TimeCommand implements Command {

    @Override
    public void execute() {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(currentTime);
    }

    @Override
    public String getDescription() {
        return "time - выводит текущее время";
    }
}
