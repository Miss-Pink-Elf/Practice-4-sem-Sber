package com.kaslanaki.sber;

import java.text.SimpleDateFormat;
import java.util.Date;

// Команда для вывода текущей даты
public class DateCommand implements Command {
    @Override
    public void execute() {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        System.out.println(currentDate);
    }

    @Override
    public String getDescription() {
        return "date - выводит текущую дату";
    }

}
