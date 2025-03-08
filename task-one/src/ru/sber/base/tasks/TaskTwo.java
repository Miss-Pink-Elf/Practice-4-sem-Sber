package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskTwo {

    public static void main(String[] args) {
        TaskTwo task2 = new TaskTwo();
        task2.exec();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите два натуральных числа a и b: ");
        
        int a = in.nextInt();
        int b = in.nextInt();
        int res = a / b;
        int rem = a % b;
        
        if (b > 0 || a > 0) {
            System.out.println(a + " / " + b + " = " + res + " и " + rem + " в остатке");
        }
        else {
            System.out.println("Вы ввели ненатуральное число");
        }

    }
}
