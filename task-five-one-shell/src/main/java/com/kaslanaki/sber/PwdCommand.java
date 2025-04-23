package com.kaslanaki.sber;

// Команда для вывода текущего рабочего каталога
public class PwdCommand implements Command {
    @Override
    public void execute() {
        System.out.println(System.getProperty("user.dir"));
    }
}
