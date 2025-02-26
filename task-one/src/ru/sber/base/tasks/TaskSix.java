package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskSix {

    public static void main(String[] args) {
        TaskSix task6 = new TaskSix();
        task6.exec();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите натуральное число: ");

        int num = in.nextInt();
        boolean isPrime = true;

        if (num <= 1) {
            isPrime = false;
        } else {
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    isPrime = false;
                    break;
                }
            }
        }

        System.out.println("Является ли число " + num + " простым: " + isPrime);
    }
}
