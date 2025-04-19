package com.kaslanaki.sber;

import java.util.Map;

// Команда для вывода списка доступных команд
public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute() {
        System.out.println("Доступные команды:");
        commands.forEach((name, command) -> System.out.println(" " + command.getDescription()));
    }

    @Override
    public String getDescription() {
        return "help - выводит список доступных команд";
    }
}
