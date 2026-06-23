package org.example.factories;

import org.example.model.Shape;
import org.example.model.Star;

public class StarFactory implements ShapeFactory {
    @Override
    public Shape create() {
        return new Star();
    }
}