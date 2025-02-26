package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskThree {

    public static void main(String[] args) {
        TaskThree task3 = new TaskThree();
        task3.exec();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите число: ");

        int num = in.nextInt();
        int n = num;
        int rev = 0;
        int dig;

        while (num > 0)
        {
            dig = num % 10;
            rev = rev * 10 + dig;
            num = num / 10;
        }

        if (n == rev) {
            System.out.println(n + " - палиндром");
        }
        else {
            System.out.println(n + " - не палиндром");
        }
    }
}
