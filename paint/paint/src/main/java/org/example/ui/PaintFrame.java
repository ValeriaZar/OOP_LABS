package org.example.ui;

import org.example.model.Shape;
import org.example.factories.*;
import org.example.serialization.BsonSerializer;
import org.example.serialization.DataProcessor;
import org.example.serialization.PluginLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PaintFrame extends JFrame {
    private List<Shape> shapes = new ArrayList<>();
    private Map<String, ShapeFactory> factories = new LinkedHashMap<>();
    private BsonSerializer serializer;

    private List<DataProcessor> dataProcessors = new ArrayList<>();
    private DataProcessor selectedProcessor = null;

    private DefaultListModel<Shape> listModel = new DefaultListModel<>();
    private JList<Shape> shapeJList = new JList<>(listModel);
    private JPanel editorContainer;
    private JPanel canvas;

    private String selectedType = "Rectangle";
    private Shape currentPreview = null;
    private float startX, startY;

    public PaintFrame() {
        setTitle("Paint");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initFactories();
        serializer = new BsonSerializer(factories);
        setupUI();
    }

    private void initFactories() {
        factories.put("Rectangle", new RectangleFactory());
        factories.put("Square", new SquareFactory());
        factories.put("Circle", new CircleFactory());
        factories.put("Ellipse", new EllipseFactory());
        factories.put("Line", new LineFactory());
        factories.put("Triangle", new TriangleFactory());

        List<ShapeFactory> plugins = PluginLoader.loadPlugins("plugins");
        for (ShapeFactory pf : plugins) {
            String typeName = pf.create().toDocument().getString("type");
            factories.put(typeName, pf);
        }

        dataProcessors = PluginLoader.loadDataPlugins("plugins");
    }

    private void setupUI() {
        setLayout(new BorderLayout());

        JPanel topContainer = new JPanel(new GridLayout(2, 1));

        JPanel shapeBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (String type : factories.keySet()) {
            JButton b = new JButton(type);
            b.addActionListener(e -> selectedType = type);
            shapeBar.add(b);
        }
        JPanel controlBar = new JPanel(new FlowLayout(FlowLayout.LEFT));

        controlBar.add(new JLabel(" Data Processor: "));
        JComboBox<String> pluginCombo = new JComboBox<>();
        pluginCombo.addItem("None");
        for (DataProcessor dp : dataProcessors) {
            pluginCombo.addItem(dp.getName());
        }
        pluginCombo.addActionListener(e -> {
            int idx = pluginCombo.getSelectedIndex();
            selectedProcessor = (idx > 0) ? dataProcessors.get(idx - 1) : null;
        });
        controlBar.add(pluginCombo);

        controlBar.add(new JSeparator(JSeparator.VERTICAL));

        JButton saveBtn = new JButton("Save Project");
        saveBtn.setBackground(new Color(200, 230, 200));
        saveBtn.addActionListener(e -> {
            try {
                serializer.serialize(shapes, "data.bson", selectedProcessor);
                String msg = selectedProcessor != null ?
                        "Successfully saved using: " + selectedProcessor.getName() : "Saved as standard BSON";
                JOptionPane.showMessageDialog(this, msg);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Save error: " + ex.getMessage());
            }
        });

        JButton loadBtn = new JButton("Load Project");
        loadBtn.setBackground(new Color(200, 220, 240));
        loadBtn.addActionListener(e -> loadFromFile());

        controlBar.add(saveBtn);
        controlBar.add(loadBtn);

        topContainer.add(shapeBar);
        topContainer.add(controlBar);
        add(topContainer, BorderLayout.NORTH);

        canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                ShapeRenderer renderer = new ShapeRenderer();
                for (Shape s : shapes) renderer.draw(g2d, s);
                if (currentPreview != null) {
                    g2d.setColor(Color.LIGHT_GRAY);
                    renderer.draw(g2d, currentPreview);
                }
            }
        };
        canvas.setBackground(Color.WHITE);

        canvas.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                startX = e.getX();
                startY = e.getY();
                if (factories.containsKey(selectedType)) {
                    currentPreview = factories.get(selectedType).create();
                }
            }
            public void mouseReleased(MouseEvent e) {
                if (currentPreview != null) {
                    shapes.add(currentPreview);
                    listModel.addElement(currentPreview);
                    currentPreview = null;
                    canvas.repaint();
                }
            }
        });

        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (currentPreview != null) {
                    currentPreview.setPoints(startX, startY, e.getX(), e.getY());
                    canvas.repaint();
                }
            }
        });
        add(canvas, BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setPreferredSize(new Dimension(300, 0));
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));

        JPanel listPanel = new JPanel(new BorderLayout());
        shapeJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listPanel.add(new JScrollPane(shapeJList), BorderLayout.CENTER);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.setBackground(new Color(255, 200, 200));
        deleteBtn.addActionListener(e -> deleteSelectedShape());
        listPanel.add(deleteBtn, BorderLayout.SOUTH);
        listPanel.setBorder(BorderFactory.createTitledBorder("Objects List"));

        rightPanel.add(listPanel, BorderLayout.NORTH);

        editorContainer = new JPanel(new BorderLayout());
        editorContainer.setBorder(BorderFactory.createTitledBorder("Properties Editor"));
        rightPanel.add(editorContainer, BorderLayout.CENTER);

        shapeJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Shape selected = shapeJList.getSelectedValue();
                editorContainer.removeAll();
                if (selected != null) {
                    editorContainer.add(selected.createEditorPanel(canvas::repaint), BorderLayout.NORTH);
                }
                editorContainer.revalidate();
                editorContainer.repaint();
            }
        });

        add(rightPanel, BorderLayout.EAST);
    }

    private void deleteSelectedShape() {
        Shape selected = shapeJList.getSelectedValue();
        if (selected != null) {
            shapes.remove(selected);
            listModel.removeElement(selected);
            editorContainer.removeAll();
            editorContainer.revalidate();
            editorContainer.repaint();
            canvas.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Select a shape in the list to delete.");
        }
    }

    private void loadFromFile() {
        try {
            List<Shape> loaded = serializer.deserialize("data.bson", selectedProcessor);
            shapes.clear();
            listModel.clear();
            shapes.addAll(loaded);
            for (Shape s : shapes) listModel.addElement(s);
            canvas.repaint();
            JOptionPane.showMessageDialog(this, "Loaded " + loaded.size() + " shapes.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Load failed! Make sure you selected the correct Processor.\nError: " + ex.getMessage(),
                    "Deserialization Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}