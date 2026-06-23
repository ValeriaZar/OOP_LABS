package org.example.factories;

import org.example.model.Shape;
import org.example.model.Square;

public class SquareFactory implements ShapeFactory {
    @Override
    public Shape create() {
        return new Square();
    }
}
