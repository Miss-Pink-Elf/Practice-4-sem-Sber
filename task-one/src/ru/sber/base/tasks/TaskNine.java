package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskNine {

    public static void main(String[] args) {
        TaskNine task9 = new TaskNine();
        task9.exec();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите три вещественных числа (коэффициенты квадратного уравнения): ");

        double a = in.nextDouble();
        double b = in.nextDouble();
        double c = in.nextDouble();
        double D = b * b - 4 * a * c;

        if (D < 0)
            System.out.println("У уравнения нет корней");
        else if (D == 0) {
            double x = (- b - Math.sqrt(D)) / (2 * a);

            System.out.println("У уравнения один корень, x = " + x);
        }
        else {
            double x1 = (- b - Math.sqrt(D)) / (2 * a);
            double x2 = (- b + Math.sqrt(D)) / (2 * a);

            System.out.println("У уравнения два корня, x1 = " + x1 + ", x2 = " + x2);
        }
    }
}
