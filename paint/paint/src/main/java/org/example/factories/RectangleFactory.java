package org.example.factories;


import org.example.model.Rectangle;
import org.example.model.Shape;

public class RectangleFactory implements ShapeFactory {
    @Override
    public Shape create() {
        return new Rectangle();
    }
}
