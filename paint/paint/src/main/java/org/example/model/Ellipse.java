package org.example.model;

import org.bson.Document;
import javax.swing.*;
import java.awt.*;
import org.example.ui.ShapeVisitor;

public class Ellipse extends Shape {
    private float x, y, w, h;

    @Override
    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x = Math.min(x1, x2);
        this.y = Math.min(y1, y2);
        this.w = Math.abs(x1 - x2);
        this.h = Math.abs(y1 - y2);
    }

    @Override
    public Document toDocument() {
        return new Document("type", "Ellipse")
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
        JTextField xf = new JTextField(String.valueOf(x));
        xf.addActionListener(e -> { this.x = Float.parseFloat(xf.getText()); repaintCallback.run(); });
        panel.add(xf);

        panel.add(new JLabel("Y:"));
        JTextField yf = new JTextField(String.valueOf(y));
        yf.addActionListener(e -> { this.y = Float.parseFloat(yf.getText()); repaintCallback.run(); });
        panel.add(yf);

        panel.add(new JLabel("W:"));
        JTextField wf = new JTextField(String.valueOf(w));
        wf.addActionListener(e -> { this.w = Float.parseFloat(wf.getText()); repaintCallback.run(); });
        panel.add(wf);

        panel.add(new JLabel("H:"));
        JTextField hf = new JTextField(String.valueOf(h));
        hf.addActionListener(e -> { this.h = Float.parseFloat(hf.getText()); repaintCallback.run(); });
        panel.add(hf);

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