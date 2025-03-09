package com.kaslanaki.sber;

public class PaintExample {
    public static void main(String[] args) {
        Point point = new Point(10, 15);
        Figure circle = new Circle(point, 5);
        Figure rectangle = new Rectangle(point, 4, 6);
        Figure square = new Square(point, 4);
        Figure triangle = new Triangle(point, 3, 4, 5);

        FigureUtil.draw(circle);
        FigureUtil.draw(rectangle, Color.DARK_PINK);
        FigureUtil.draw(square);
        FigureUtil.draw(triangle, Color.CORAL_PINK);

        System.out.println("Площадь круга: " + FigureUtil.area(circle));
        System.out.println("Периметр прямоугольника: " + FigureUtil.perimeter(rectangle));
    }
}
