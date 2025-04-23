package com.kaslanaki.sber;

import java.text.SimpleDateFormat;
import java.util.Date;

@CommandAnno(name = "date", description = "вывод текущей даты")
public class DateCommand implements Command {

    @Override
    public String getName() {
        return "date";
    }

    @Override
    public void execute(String[] args) {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        System.out.println(currentDate);
    }

    @Override
    public String getHelp() {
        return "выводит текущую дату";
    }
}
