package org.example.model;

import org.bson.Document;
import org.example.ui.ShapeVisitor;
import javax.swing.*;
import java.io.Serializable;

public abstract class Shape implements Serializable {
    public abstract void accept(ShapeVisitor visitor);
    public abstract Document toDocument();
    public abstract void fromDocument(Document doc);
    public abstract JPanel createEditorPanel(Runnable repaintCallback);
    public abstract void setPoints(float x1, float y1, float x2, float y2);
}