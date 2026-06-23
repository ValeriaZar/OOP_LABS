package org.example.model;

import org.bson.Document;
import org.example.ui.Drawable;
import org.example.ui.ShapeVisitor;
import javax.swing.*;
import java.awt.*;

public class Star extends Shape implements Drawable {
    private float x, y, size;

    @Override
    public void setPoints(float x1, float y1, float x2, float y2) {
        this.x = Math.min(x1, x2);
        this.y = Math.min(y1, y2);
        this.size = Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    @Override
    public Document toDocument() {
        return new Document("type", "Star")
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
        JPanel panel = new JPanel(new GridLayout(1, 2));
        panel.add(new JLabel("Size:"));
        JTextField sField = new JTextField(String.valueOf(size));
        sField.addActionListener(e -> {
            this.size = Float.parseFloat(sField.getText());
            repaintCallback.run();
        });
        panel.add(sField);
        return panel;
    }

    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void draw(Graphics2D g2d) {
        int xCenter = (int)(x + size/2);
        int yCenter = (int)(y + size/2);
        int outer = (int)(size/2);
        int inner = outer / 2;
        int[] px = new int[10];
        int[] py = new int[10];
        for (int i = 0; i < 10; i++) {
            double angle = Math.PI * i / 5 - Math.PI / 2;
            int r = (i % 2 == 0) ? outer : inner;
            px[i] = (int)(xCenter + r * Math.cos(angle));
            py[i] = (int)(yCenter + r * Math.sin(angle));
        }
        g2d.drawPolygon(px, py, 10);
    }
}