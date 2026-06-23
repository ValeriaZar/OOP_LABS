package org.example.model;

import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import org.example.ui.ShapeVisitor;

public class Line extends Shape {
    private float x1, y1, x2, y2;

    @Override
    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x1 = x1; this.y1 = y1;
        this.x2 = x2; this.y2 = y2;
    }

    @Override
    public Document toDocument() {
        return new Document("type", "Line")
                .append("x1", (double)x1).append("y1", (double)y1)
                .append("x2", (double)x2).append("y2", (double)y2);
    }

    @Override
    public void fromDocument(Document doc) {
        this.x1 = doc.getDouble("x1").floatValue();
        this.y1 = doc.getDouble("y1").floatValue();
        this.x2 = doc.getDouble("x2").floatValue();
        this.y2 = doc.getDouble("y2").floatValue();
    }

    @Override
    public JPanel createEditorPanel(Runnable repaintCallback) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("X1:"));
        JTextField x1f = new JTextField(String.valueOf(x1));
        x1f.addActionListener(e -> { this.x1 = Float.parseFloat(x1f.getText()); repaintCallback.run(); });
        panel.add(x1f);

        panel.add(new JLabel("Y1:"));
        JTextField y1f = new JTextField(String.valueOf(y1));
        y1f.addActionListener(e -> { this.y1 = Float.parseFloat(y1f.getText()); repaintCallback.run(); });
        panel.add(y1f);

        panel.add(new JLabel("X2:"));
        JTextField x2f = new JTextField(String.valueOf(x2));
        x2f.addActionListener(e -> { this.x2 = Float.parseFloat(x2f.getText()); repaintCallback.run(); });
        panel.add(x2f);

        panel.add(new JLabel("Y2:"));
        JTextField y2f = new JTextField(String.valueOf(y2));
        y2f.addActionListener(e -> { this.y2 = Float.parseFloat(y2f.getText()); repaintCallback.run(); });
        panel.add(y2f);

        return panel;
    }

    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }

    public float getX1() { return x1; }
    public float getY1() { return y1; }
    public float getX2() { return x2; }
    public float getY2() { return y2; }
}