package org.example.factories;


import org.example.model.Shape;
import org.example.model.Triangle;

public class TriangleFactory implements ShapeFactory {
    @Override
    public Shape create() {
        return new Triangle();
    }
}
