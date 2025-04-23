package com.kaslanaki.sber;

// Команда для вывода списка доступных команд
public class HelpCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Доступные команды:");
        System.out.println("time - выводит текущее время");
        System.out.println("pwd - выводит текущий рабочий каталог");
        System.out.println("exit - завершает работу приложения");
        System.out.println("help - выводит список доступных команд");
        System.out.println("date - выводит текущую дату");
    }

}
