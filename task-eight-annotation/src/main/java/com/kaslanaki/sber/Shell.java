package com.kaslanaki.sber;

import com.kaslanaki.sber.Commands.help;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Shell {

    private final Map<String, Command> commands = new HashMap<>();

    public Shell() {
        Reflections reflections = new Reflections("com.kaslanaki.sber");

        for (Class<?> clazz : reflections.getTypesAnnotatedWith(CommandAnno.class)) {
            if (Command.class.isAssignableFrom(clazz)) {
                CommandAnno anno = clazz.getAnnotation(CommandAnno.class);
                try {
                    Command cmdInstance;
                    if (clazz == help.class) {
                        cmdInstance = (Command) clazz.getConstructor(Map.class).newInstance(commands);
                    } else {
                        cmdInstance = (Command) clazz.getDeclaredConstructor().newInstance();
                    }
                    commands.put(anno.name(), cmdInstance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    System.err.println("Ошибка при создании команды " + clazz.getSimpleName() + ": " + e.getMessage());
                }
            }
        }
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите команду (или help для того, чтобы узнать доступные команды):");

        while (true) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\s+");
            String cmdName = parts[0];
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, args.length);

            Command command = commands.get(cmdName);
            if (command != null) {
                try {
                    command.execute(args);
                } catch (Exception e) {
                    System.out.println("Ошибка выполнения команды: " + e.getMessage());
                }
            } else {
                System.out.println("Команда не найдена: " + cmdName);
            }
        }
    }
    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.start();
    }
}
