package com.kaslanaki.sber;

class MathOperations {
    public int multiplyEvenNumbers(int num1, int num2) throws checkedNumbers {
        if (num1 % 2 != 0 || num2 % 2 != 0) {
            throw new checkedNumbers("Введено нечетное число");
        }
        return num1 * num2;
    }
}
