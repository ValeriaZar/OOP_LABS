package org.example.factories;


import org.example.model.Ellipse;
import org.example.model.Shape;

public class EllipseFactory implements ShapeFactory{
    @Override
    public Shape create() {
        return new Ellipse();
    }
}
