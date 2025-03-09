package com.kaslanaki.sber;

class Rectangle extends Figure implements Drawable {
    private double width;
    private double height;

    public Rectangle(Point point, double width, double height) {
        super(point);
        this.width = width;
        this.height = height;
    }

    @Override
    public double area() {
        return width * height;
    }

    @Override
    public double perimeter() {
        return 2 * (width + height);
    }

    @Override
    public void draw() {
        System.out.println("Нарисован прямоугольник с координатами " + point.getX() + ", " + point.getY());
    }

    @Override
    public void draw(Color color) {
        System.out.println("Нарисован прямоугольник цветом " + color + " с координатами " + point.getX() + ", " + point.getY());
    }
}
