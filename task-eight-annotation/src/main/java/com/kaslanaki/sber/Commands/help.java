package com.kaslanaki.sber.Commands;

import com.kaslanaki.sber.Command;
import com.kaslanaki.sber.CommandAnno;

import java.util.Map;

@CommandAnno(name = "help", description = "вывод списка доступных команд")
public class help implements Command {

    private final Map<String, Command> commands;

    public help(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(String[] args) {
        System.out.println("Доступные команды:");
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            CommandAnno anno = entry.getValue().getClass().getAnnotation(CommandAnno.class);
            String description = (anno != null) ? anno.description() : "";
            System.out.printf("%s - %s%n", entry.getKey(), description);
        }
    }
}
