package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskFour {

    public static void main(String[] args) {
        TaskFour task4 = new TaskFour();
        task4.exec();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите число натуральное трёхзначное число: ");

        int num = in.nextInt();
        int sum_num = 0;
        int n = num;

        if (num > 99 && num < 1000) {
            while (num > 0) {
                sum_num += num % 10;
                num /= 10;
            }
            System.out.println("Сумма цифр числа " + n + " равна " + sum_num);
        }
        else {
            System.out.println("Вы ввели нетрёхзначное или ненатуральное число");
        }
    }
}
