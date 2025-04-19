package com.kaslanaki.sber;

import java.util.ArrayList;
import java.util.List;

class GenericCollection<T> {
    private List<T> elements;

    public GenericCollection() {
        elements = new ArrayList<>();
    }

    public void add(T element) {
        elements.add(element);
    }

    public void remove(int index) {
        if (index >= 0 && index < elements.size()) {
            elements.remove(index);
        } else {
            System.out.println("Индекс вне диапазона");
        }
    }

    public T get(int index) {
        if (index >= 0 && index < elements.size()) {
            return elements.get(index);
        } else {
            System.out.println("Индекс вне диапазона");
            return null;
        }
    }

    public int size() {
        return elements.size();
    }

    public void printAll() {
        for (T element : elements) {
            System.out.println(element);
        }
    }
}