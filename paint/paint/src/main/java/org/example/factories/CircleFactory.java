package org.example.factories;

import org.example.model.Circle;
import org.example.model.Shape;

public class CircleFactory implements ShapeFactory{
    @Override
    public Shape create() {
        return new Circle();
    }
}
