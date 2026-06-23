package org.example.ui;

import org.example.model.*;

public interface ShapeVisitor {
    void visit(Rectangle r);
    void visit(Circle c);
    void visit(Triangle t);
    void visit(Ellipse e);
    void visit(Line l);
    void visit(Square s);
    void visit(Shape s);
}
