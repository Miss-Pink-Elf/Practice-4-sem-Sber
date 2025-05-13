package com.kaslanaki.sber;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Shell {
    private final List<Command> commands = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public Shell() {
        loadCommands();
    }

    private void loadCommands() {
        try {
            Reflections reflections = new Reflections("com.kaslanaki.sber");
            Set<Class<? extends Command>> commandClasses = reflections.getSubTypesOf(Command.class);

            for (Class<? extends Command> commandClass : commandClasses) {
                if (!Modifier.isAbstract(commandClass.getModifiers())) {
                    Constructor<? extends Command> constructor = commandClass.getConstructor();
                    Command command = constructor.newInstance();
                    commands.add(command);
                }
            }
        } catch (Exception e) {
            System.out.println("Ошибка при загрузке команд: " + e.getMessage());
        }
    }

    public void start() {
        System.out.println("Добро пожаловать в Shell. Введите команду или 'help' для списка команд.");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            String[] inputArgs = input.trim().split(" ");
            String commandName = inputArgs[0];

            if ("exit".equalsIgnoreCase(commandName)) {
                System.out.println("Выход из приложения...");
                break;
            } else if ("help".equalsIgnoreCase(commandName)) {
                if (inputArgs.length == 1) {
                    showHelp();
                } else {
                    showHelpForCommand(inputArgs[1]);
                }
            } else {
                executeCommand(commandName, inputArgs);
            }
        }
    }

    private void executeCommand(String commandName, String[] args) {
        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(commandName)) {
                command.execute(args);
                return;
            }
        }
        System.out.println("Неизвестная команда: " + commandName);
    }

    private void showHelp() {
            System.out.println("Доступные команды:");
            for (Command command : commands) {
                System.out.println(command.getName() + ": " + command.getHelp());
            }
        }

    private void showHelpForCommand(String commandName) {
        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(commandName)) {
                System.out.println(command.getHelp());
                return;
            }
        }
        System.out.println("Неизвестная команда: " + commandName);
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.start();
    }
}
