package com.kaslanaki.sber;

public class FigureUtil {
    private FigureUtil() {}

    public static double area(Figure figure) {
        return figure.area();
    }

    public static double perimeter(Figure figure) {
        return figure.perimeter();
    }

    public static void draw(Figure figure) {
        System.out.println("Нарисована фигура: " + figure.getClass().getSimpleName() + " с координатами " +
                figure.point.getX() + ", " + figure.point.getY() + " в чёрном цвете.");
    }

    public static void draw(Figure figure, Color color) {
        System.out.println("Нарисована фигура: " + figure.getClass().getSimpleName() + " с координатами " +
                figure.point.getX() + ", " + figure.point.getY() + " в цвете " + color + ".");
    }

}
