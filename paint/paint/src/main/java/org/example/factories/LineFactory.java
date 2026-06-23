package org.example.factories;

import org.example.model.Line;
import org.example.model.Shape;

public class LineFactory implements ShapeFactory{
    @Override
    public Shape create() {
        return new Line();
    }
}
