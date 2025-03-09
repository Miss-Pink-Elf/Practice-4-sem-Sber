package com.kaslanaki.sber;

public class PaintExample {
    public static void main(String[] args) {
        Point point = new Point(10, 15);
        Figure circle = new Circle(point, 5);
        Figure rectangle = new Rectangle(point, 4, 6);
        Point point2 = new Point(1, 5);
        Figure square = new Square(point2, 4);
        Figure triangle = new Triangle(point2, 3, 4, 5);

        FigureUtil.draw(circle);
        FigureUtil.draw(rectangle, Color.DARK_PINK);
        FigureUtil.draw(square);
        FigureUtil.draw(triangle, Color.CORAL_PINK);

        System.out.println("Площадь круга: " + FigureUtil.area(circle));
        System.out.println("Периметр прямоугольника: " + FigureUtil.perimeter(rectangle));
        System.out.println("Площадь треугольника: " + FigureUtil.area(triangle));
        System.out.println("Периметр треугольника: " + FigureUtil.perimeter(triangle));
    }
}
