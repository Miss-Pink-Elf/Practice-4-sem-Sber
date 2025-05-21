package com.kaslanaki;
import java.util.List;
import java.util.Scanner;

public class ToDoApp {
    private static final TaskDao taskDao = new TaskDao();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        DatabaseManager.initializeDatabase();

        runApp();
    }

    private static void runApp() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Введите команду: ");
            String input = scanner.nextLine().trim();
            String[] parts = input.split(" ", 2);
            String command = parts[0].toLowerCase();

            try {
                switch (command) {
                    case "add":
                        if (parts.length > 1 && !parts[1].isBlank()) {
                            taskDao.addTask(parts[1]);
                        } else {
                            System.out.println("Пожалуйста, укажите описание задачи. Пример: add Купить молоко");
                        }
                        break;
                    case "show":
                        showTasks();
                        break;
                    case "done":
                        if (parts.length > 1) {
                            try {
                                int idToMarkDone = Integer.parseInt(parts[1]);
                                taskDao.markTaskAsDone(idToMarkDone);
                            } catch (NumberFormatException e) {
                                System.out.println("Неверный ID задачи. Введите число. Пример: done 1");
                            }
                        } else {
                            System.out.println("Пожалуйста, укажите ID задачи для завершения. Пример: done 1");
                        }
                        break;
                    case "delete":
                        if (parts.length > 1) {
                            try {
                                int idToDelete = Integer.parseInt(parts[1]);
                                taskDao.deleteTask(idToDelete);
                            } catch (NumberFormatException e) {
                                System.out.println("Неверный ID задачи. Введите число. Пример: delete 1");
                            }
                        } else {
                            System.out.println("Пожалуйста, укажите ID задачи для удаления. Пример: delete 1");
                        }
                        break;
                    case "help":
                        printHelp();
                        break;
                    case "exit":
                        running = false;
                        System.out.println("Выход из приложения ToDo List.");
                        break;
                    default:
                        System.out.println("Неизвестная команда. Введите 'help' для списка команд.");
                }
            } catch (Exception e) {
                System.err.println("Произошла ошибка при выполнении команды: " + e.getMessage());
                e.printStackTrace();
            }
            if (running) System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("===== ToDo List Menu =====");
        System.out.println("Доступные команды:");
        System.out.println("  add <описание задачи> - Добавить новую задачу");
        System.out.println("  show                  - Показать все задачи");
        System.out.println("  done <ID задачи>      - Отметить задачу как выполненную");
        System.out.println("  delete <ID задачи>    - Удалить задачу");
        System.out.println("  help                  - Показать эту справку");
        System.out.println("  exit                  - Выйти из приложения");
        System.out.println("==========================");
    }
    private static void printHelp() {
        printMenu();
    }

    private static void showTasks() {
        List<Task> tasks = taskDao.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            System.out.println("--- Ваш список задач ---");
            for (Task task : tasks) {
                System.out.println(task);
            }
            System.out.println("------------------------");
        }
    }
}