package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskFive {

    public static void main(String[] args) {
        TaskFive task5 = new TaskFive();
        task5.exec();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите земной вес(массу) в кг, чтобы узнать чему он равен на Луне: ");

        double weight_Earth = in.nextDouble();
        double weight_Moon = weight_Earth * 0.17;

        System.out.println("Ответ: " + weight_Moon + " кг");
    }
}
