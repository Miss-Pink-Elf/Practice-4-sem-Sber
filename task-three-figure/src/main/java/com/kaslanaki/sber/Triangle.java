package com.kaslanaki.sber;

class Triangle extends Figure implements Drawable {
    private double a, b, c;

    public Triangle(Point point, double a, double b, double c) {
        super(point);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public double area() {
        double s = perimeter() / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }

    @Override
    public double perimeter() {
        return a + b + c;
    }

    @Override
    public void draw() {
        System.out.println("Нарисован треугольник с координатами " + point.getX() + ", " + point.getY());
    }

    @Override
    public void draw(Color color) {
        System.out.println("Нарисован треугольник цветом " + color + " с координатами " + point.getX() + ", " + point.getY());
    }
}

