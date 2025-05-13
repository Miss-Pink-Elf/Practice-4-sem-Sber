package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;
import com.kaslanaki.sber.CommandAnno;

import java.text.SimpleDateFormat;
import java.util.Date;

@CommandAnno(name = "date", description = "вывод текущей даты")
public class date implements Command {

    @Override
    public void execute(String[] args) {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        System.out.println(currentDate);
    }

}
