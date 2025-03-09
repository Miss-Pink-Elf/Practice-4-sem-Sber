package com.kaslanaki.sber;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PaintTest {

    @Test
    public void testCircleArea() {
        Point point = new Point(0, 0);
        Circle circle = new Circle(point, 5);
        assertEquals(Math.PI * 5 * 5, circle.area(), 0.0001);
    }

    @Test
    public void testCirclePerimeter() {
        Point point = new Point(0, 0);
        Circle circle = new Circle(point, 5);
        assertEquals(2 * Math.PI * 5, circle.perimeter(), 0.0001);
    }

    @Test
    public void testRectangleArea() {
        Point point = new Point(1, 1);
        Rectangle rectangle = new Rectangle(point, 4, 3);
        assertEquals(12, rectangle.area());
    }

    @Test
    public void testRectanglePerimeter() {
        Point point = new Point(1, 1);
        Rectangle rectangle = new Rectangle(point, 4, 3);
        assertEquals(14, rectangle.perimeter());
    }

    @Test
    public void testSquareArea() {
        Point point = new Point(2, 2);
        Square square = new Square(point, 4);
        assertEquals(16, square.area());
    }

    @Test
    public void testSquarePerimeter() {
        Point point = new Point(2, 2);
        Square square = new Square(point, 4);
        assertEquals(16, square.perimeter());
    }

    @Test
    public void testTriangleArea() {
        Point point = new Point(3, 3);
        Triangle triangle = new Triangle(point, 3, 4, 5);
        assertEquals(6, triangle.area(), 0.0001); // Площадь треугольника с сторонами 3, 4, 5
    }

    @Test
    public void testTrianglePerimeter() {
        Point point = new Point(3, 3);
        Triangle triangle = new Triangle(point, 3, 4, 5);
        assertEquals(12, triangle.perimeter());
    }

    @Test
    public void testFigureUtilArea() {
        Point point = new Point(1, 1);
        Circle circle = new Circle(point, 5);
        assertEquals(Math.PI * 5 * 5, FigureUtil.area(circle), 0.0001);
    }

    @Test
    public void testFigureUtilPerimeter() {
        Point point = new Point(1, 1);
        Rectangle rectangle = new Rectangle(point, 4, 3);
        assertEquals(14, FigureUtil.perimeter(rectangle));
    }

    @Test
    public void testFigureUtilDraw() {
        Point point = new Point(1, 1);
        Circle circle = new Circle(point, 5);
        FigureUtil.draw(circle);
    }

    @Test
    public void testFigureUtilDrawWithColor() {
        Point point = new Point(1, 1);
        Rectangle rectangle = new Rectangle(point, 4, 3);
        FigureUtil.draw(rectangle, Color.RED);
    }
}