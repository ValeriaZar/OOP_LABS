package org.example.ui;

import org.example.model.*;
import org.example.model.Rectangle;
import org.example.model.Shape;
import java.awt.*;

public class ShapeRenderer implements ShapeVisitor {
    private Graphics2D g2d;

    public void draw(Graphics2D g2d, Shape shape) {
        this.g2d = g2d;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2f));
        shape.accept(this);
    }

    @Override
    public void visit(Circle c) {
        g2d.drawOval((int)c.getX(), (int)c.getY(), (int)c.getDiameter(), (int)c.getDiameter());
    }

    @Override
    public void visit(Rectangle r) {
        g2d.drawRect((int)r.getX(), (int)r.getY(), (int)r.getW(), (int)r.getH());
    }

    @Override
    public void visit(Line l) {
        g2d.drawLine((int)l.getX1(), (int)l.getY1(), (int)l.getX2(), (int)l.getY2());
    }

    @Override
    public void visit(Square s) {
        g2d.drawRect((int)s.getX(), (int)s.getY(), (int)s.getSize(), (int)s.getSize());
    }

    @Override
    public void visit(Ellipse e) {
        g2d.drawOval((int)e.getX(), (int)e.getY(), (int)e.getW(), (int)e.getH());
    }

    @Override
    public void visit(Triangle t) {
        int[] xPoints = {(int)t.getX1(), (int)t.getX2(), (int)t.getX3()};
        int[] yPoints = {(int)t.getY1(), (int)t.getY2(), (int)t.getY3()};
        g2d.drawPolygon(xPoints, yPoints, 3);
    }

    @Override
    public void visit(Shape s) {
        if (s instanceof Drawable) {
            ((Drawable) s).draw(this.g2d);
        }
    }
}