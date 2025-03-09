package com.kaslanaki.sber;

class Square extends Rectangle implements Drawable {
    public Square(Point point, double side) {
        super(point, side, side);
    }

    @Override
    public void draw() {
        System.out.println("Нарисован квадрат с координатами " + point.getX() + ", " + point.getY());
    }

    @Override
    public void draw(Color color) {
        System.out.println("Нарисован квадрат цветом " + color + " с координатами " + point.getX() + ", " + point.getY());
    }
}
