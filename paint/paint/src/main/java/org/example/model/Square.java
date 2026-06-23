package org.example.model;

import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import org.example.ui.ShapeVisitor;

public class Square extends Shape {
    private float x, y, size;

    @Override
    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x = Math.min(x1, x2);
        this.y = Math.min(y1, y2);
        this.size = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    @Override
    public Document toDocument() {
        return new Document("type", "Square")
                .append("x", (double)x).append("y", (double)y)
                .append("size", (double)size);
    }

    @Override
    public void fromDocument(Document doc) {
        this.x = doc.getDouble("x").floatValue();
        this.y = doc.getDouble("y").floatValue();
        this.size = doc.getDouble("size").floatValue();
    }

    @Override
    public JPanel createEditorPanel(Runnable repaintCallback) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.add(new JLabel("X:"));
        JTextField xField = new JTextField(String.valueOf(x));
        xField.addActionListener(e -> { this.x = Float.parseFloat(xField.getText()); repaintCallback.run(); });
        panel.add(xField);

        panel.add(new JLabel("Y:"));
        JTextField yField = new JTextField(String.valueOf(y));
        yField.addActionListener(e -> { this.y = Float.parseFloat(yField.getText()); repaintCallback.run(); });
        panel.add(yField);

        panel.add(new JLabel("Size:"));
        JTextField sField = new JTextField(String.valueOf(size));
        sField.addActionListener(e -> { this.size = Float.parseFloat(sField.getText()); repaintCallback.run(); });
        panel.add(sField);

        return panel;
    }

    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getSize() { return size; }
}