package com.kaslanaki.sber;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ClassAnalyzer {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        String className = args[0];

        try {
            Class<?> clazz = Class.forName(className);

            analyzeAndPrintClass(clazz);

        } catch (ClassNotFoundException e) {
            System.err.println("Ошибка: Класс не найден: " + className);
        } catch (LinkageError e) {
            System.err.println("Ошибка связывания при загрузке класса " + className + ": " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка при анализе класса " + className + ": " + e.getMessage());
        }
    }

    private static void printUsage() {
        System.out.println("Использование: java ClassAnalyzer <полное-имя-класса>");
        System.out.println("Пример: java ClassAnalyzer java.util.ArrayList");
        // из коренной папки: java -cp src/main/java com.kaslanaki.sber.ClassAnalyzer java.util.ArrayList
    }

    private static void analyzeAndPrintClass(Class<?> clazz) {
        System.out.println("Отчет по анализу класса: " + clazz.getName());
        System.out.println("==================================================");

        Package pkg = clazz.getPackage();
        if (pkg != null) {
            System.out.println("Пакет: " + pkg.getName());
        } else {
            System.out.println("Пакет: (default)");
        }

        int classModifiers = clazz.getModifiers();
        System.out.println("Модификаторы класса: " + Modifier.toString(classModifiers));

        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            System.out.println("Суперкласс: " + superclass.getName());
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            System.out.println("Реализованные интерфейсы:");
            for (Class<?> iface : interfaces) {
                System.out.println("  - " + iface.getName());
            }
        }
        System.out.println("--------------------------------------------------");

        Field[] fields = clazz.getDeclaredFields();
        System.out.println("\nПоля класса (" + fields.length + "):");
        if (fields.length == 0) {
            System.out.println("  Нет полей, объявленных в этом классе.");
        } else {
            for (Field field : fields) {
                System.out.printf("  %s %s %s\n",
                        Modifier.toString(field.getModifiers()),
                        field.getType().getSimpleName(),
                        field.getName());
            }
        }
        System.out.println("--------------------------------------------------");
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        System.out.println("\nКонструкторы класса (" + constructors.length + "):");
        if (constructors.length == 0) {
            System.out.println("  Нет конструкторов, объявленных в этом классе.");
        } else {
            for (Constructor<?> constructor : constructors) {
                String params = Arrays.stream(constructor.getParameters())
                        .map(param -> formatParameter(param))
                        .collect(Collectors.joining(", "));
                System.out.printf("  %s %s(%s)\n",
                        Modifier.toString(constructor.getModifiers()),
                        clazz.getSimpleName(),
                        params);
            }
        }
        System.out.println("--------------------------------------------------");

        Method[] methods = clazz.getDeclaredMethods();
        System.out.println("\nМетоды класса (" + methods.length + "):");
        if (methods.length == 0) {
            System.out.println("  Нет методов, объявленных в этом классе.");
        } else {
            for (Method method : methods) {
                String params = Arrays.stream(method.getParameters())
                        .map(param -> formatParameter(param))
                        .collect(Collectors.joining(", "));

                System.out.printf("  %s %s %s(%s)\n",
                        Modifier.toString(method.getModifiers()),
                        method.getReturnType().getSimpleName(),
                        method.getName(),
                        params);
            }
        }
        System.out.println("==================================================");
        System.out.println("Конец отчета.");
    }

    private static String formatParameter(Parameter parameter) {
        String paramName = parameter.isNamePresent() ? parameter.getName() : "arg" + parameter.hashCode();
        if (!parameter.isNamePresent() || parameter.getName().matches("arg\\d+")) {
            return parameter.getType().getSimpleName();
        }
        return parameter.getType().getSimpleName() + " " + parameter.getName();
    }
}