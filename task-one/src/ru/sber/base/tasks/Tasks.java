package ru.sber.base.tasks;

import java.util.Scanner;
import java.util.Random; // for 8

public class Tasks {

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.println("Введите номер задания: ");
        int n = in.nextInt();

        switch (n) {
            case 1:
                taskOne();
                break;
            case 2:
                taskTwo();
                break;
            case 3:
                taskThree();
                break;
            case 4:
                taskFour();
                break;
            case 5:
                taskFive();
                break;
            case 6:
                taskSix();
                break;
            case 7:
                taskSeven();
                break;
            case 8:
                taskEight();
                break;
            case 9:
                taskNine();
                break;
            case 10:
                taskTen();
                break;
            default:
                System.out.println("Вы ввели номер несуществующего задания");
        }

    }

    static void taskOne() {

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

    static void taskTwo() {
        /*
        int age;
        age = 19;

        System.out.println("Возраст: " + age);
        */

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

    static void taskThree() {
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

    static void taskFour() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите число: ");

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

    static void taskFive() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите земной вес(массу) в кг, чтобы узнать чему он равен на Луне: ");

        double weight_Earth = in.nextDouble();
        double weight_Moon = weight_Earth * 0.17;

        System.out.println("Ответ: " + weight_Moon + " кг");

    }

    static void taskSix() {
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

    static void taskSeven() {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите число n (2 < n < 100): ");

        int n = in.nextInt();
        int[] arr = new int[n];
        arr[0] = 0;
        arr[1] = 1;
        for (int i = 2; i < arr.length; ++i) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }

        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }

    static void taskEight() {
        Random random = new Random();
        char HiddenLetter = (char) (random.nextInt(26) + 'A');
//        char HiddenLetter = 'R';

        Scanner in = new Scanner(System.in);
        System.out.println("Введите букву от A до Z: ");

        char ch;
        while (true) {
            ch = in.nextLine().toUpperCase().charAt(0);

            if (ch == HiddenLetter) {
                System.out.println("Right!");
                break;
            }
            else if (ch < HiddenLetter)
                System.out.println("You're too low");
            else
                System.out.println("You're too high");
        }
    }

    static void taskNine() {
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

    public static String intToRoman(int num) {
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

    static void taskTen() {
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
