package com.kaslanaki.sber;

import java.text.SimpleDateFormat;
import java.util.Date;

@CommandAnno(name = "time", description = "вывод теĸущего времени")
public class TimeCommand implements Command {
    @Override
    public String getName() {
        return "time";
    }

    @Override
    public void execute(String[] args) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(currentTime);
    }

    @Override
    public String getHelp() {
        return "вывод теĸущего времени";
    }
}
