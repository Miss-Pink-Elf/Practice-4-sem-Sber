package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskSeven {

    public static void main(String[] args) {
        TaskSeven task7 = new TaskSeven();
        task7.exec();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите число n (2 < n < 100): ");

        int n = in.nextInt();
        long[] arr = new long[n];
        arr[0] = 0;
        arr[1] = 1;

        for (int i = 2; i < arr.length; ++i) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}
