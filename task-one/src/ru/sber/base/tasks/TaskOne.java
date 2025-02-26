package ru.sber.base.tasks;

public class TaskOne {

    public static void main(String[] args) {
        TaskOne task1 = new TaskOne();
        task1.exec();
    }

    public void exec() {

        System.out.print("Hello, World!\n");
        System.out.println("\tI really want to sleep,\n\t but I can still work.");
        System.out.println("\"\"");
        System.out.println("\\");
        System.out.println("This is just an example!!");

        // comment

        /*
        big
        comment
         */

    }
}
