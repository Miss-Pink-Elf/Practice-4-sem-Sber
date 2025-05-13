package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;
import com.kaslanaki.sber.CommandAnno;

import java.text.SimpleDateFormat;
import java.util.Date;

@CommandAnno(name = "time", description = "вывод текущего времени" )
public class time implements Command {
    @Override
    public void execute(String[] args) {
        String currentTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        System.out.println(currentTime);
    }
}
