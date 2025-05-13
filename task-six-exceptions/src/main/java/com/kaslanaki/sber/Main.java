package com.kaslanaki.sber;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MathOperations mathOps = new MathOperations();
        try {
            System.out.print("Введите первое число: ");
            int num1 = scanner.nextInt();
            System.out.print("Введите второе число: ");
            int num2 = scanner.nextInt();

            int result = mathOps.multiplyEvenNumbers(num1, num2);
            System.out.println("Произведение четных чисел: " + result);
        } catch (checkedNumbers e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка ввода. Пожалуйста, введите целые числа.");
        } finally {
            System.out.println("Работа программы завершена.");
            scanner.close();
        }
    }
}
