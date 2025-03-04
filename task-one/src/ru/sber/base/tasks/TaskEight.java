package ru.sber.base.tasks;

import java.util.Random;
import java.util.Scanner;

public class TaskEight {

    public static void main(String[] args) {
        TaskEight task8 = new TaskEight();
        task8.exec();
    }

    public void exec() {
        Random random = new Random();
        char HiddenLetter = (char) (random.nextInt(26) + 'A');

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
}
