package org.example.model;

import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import org.example.ui.ShapeVisitor;

public class Rectangle extends Shape {
    private float x, y, w, h;

    public Rectangle() {}

    @Override
    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x = Math.min(x1, x2);
        this.y = Math.min(y1, y2);
        this.w = Math.abs(x1 - x2);
        this.h = Math.abs(y1 - y2);
    }

    @Override
    public Document toDocument() {
        return new Document("type", "Rectangle")
                .append("x", (double)x).append("y", (double)y)
                .append("w", (double)w).append("h", (double)h);
    }

    @Override
    public void fromDocument(Document doc) {
        this.x = doc.getDouble("x").floatValue();
        this.y = doc.getDouble("y").floatValue();
        this.w = doc.getDouble("w").floatValue();
        this.h = doc.getDouble("h").floatValue();
    }

    @Override
    public JPanel createEditorPanel(Runnable repaintCallback) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("X:"));
        JTextField xField = new JTextField(String.valueOf(x));
        xField.addActionListener(e -> { this.x = Float.parseFloat(xField.getText()); repaintCallback.run(); });
        panel.add(xField);

        panel.add(new JLabel("Y:"));
        JTextField yField = new JTextField(String.valueOf(y));
        yField.addActionListener(e -> { this.y = Float.parseFloat(yField.getText()); repaintCallback.run(); });
        panel.add(yField);

        panel.add(new JLabel("Width:"));
        JTextField wField = new JTextField(String.valueOf(w));
        wField.addActionListener(e -> { this.w = Float.parseFloat(wField.getText()); repaintCallback.run(); });
        panel.add(wField);

        panel.add(new JLabel("Height:"));
        JTextField hField = new JTextField(String.valueOf(h));
        hField.addActionListener(e -> { this.h = Float.parseFloat(hField.getText()); repaintCallback.run(); });
        panel.add(hField);

        return panel;
    }

    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getW() { return w; }
    public float getH() { return h; }
}