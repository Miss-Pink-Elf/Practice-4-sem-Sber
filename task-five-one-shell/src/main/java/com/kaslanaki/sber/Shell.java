package com.kaslanaki.sber;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// Главный класс приложения
public class Shell {
    private final Map<String, Command> commands = new HashMap<>();

    public Shell() {
        commands.put("date", new DateCommand());
        commands.put("time", new TimeCommand());
        commands.put("pwd", new PwdCommand());
        commands.put("exit", new ExitCommand());
        commands.put("help", new HelpCommand());
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            Command command = commands.get(input.trim());
            if (command != null) {
                command.execute();
            } else {
                System.out.printf("Ошибка: неизвестная команда '%s'%n", input.trim());
            }
        }
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }
}
