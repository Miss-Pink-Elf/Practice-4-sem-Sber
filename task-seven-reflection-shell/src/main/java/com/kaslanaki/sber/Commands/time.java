package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;

import java.text.SimpleDateFormat;
import java.util.Date;

public class time implements Command {
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
