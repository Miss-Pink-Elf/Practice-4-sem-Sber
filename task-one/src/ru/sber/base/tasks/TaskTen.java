package ru.sber.base.tasks;

import java.util.Scanner;

public class TaskTen {

    public static void main(String[] args) {
        TaskTen task10 = new TaskTen();
        task10.exec();
    }

    public String intToRoman(int num) {
        StringBuilder roman = new StringBuilder();
        int[] values = {100, 90, 80, 70, 60, 50, 40, 30, 20, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        String[] symbols = {"C", "XC", "LXXX", "LXX", "LX", "L", "XL", "XXX", "XX", "X", "IX", "VIII", "VII", "VI", "V",
                "IV", "III", "II", "I"};

        for (int i = 0; i < values.length; i++)
            while (num >= values[i]) {
                roman.append(symbols[i]);
                num -= values[i];
            }

        return roman.toString();
    }

    public void exec() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите натуральное число (< 100): ");
        int number = in.nextInt();

        if (number >= 1 && number <= 100) {
            System.out.println("Римская запись числа: " + intToRoman(number));
        } else {
            System.out.println("Число должно быть в диапазоне от 1 до 100.");
        }
    }
}
