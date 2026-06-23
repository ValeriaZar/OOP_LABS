package org.example.serialization;

import org.example.model.Shape;
import org.example.factories.ShapeFactory;
import org.bson.Document;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BsonSerializer {
    private final Map<String, ShapeFactory> factories;

    public BsonSerializer(Map<String, ShapeFactory> factories) {
        this.factories = factories;
    }

    public void serialize(List<Shape> shapes, String filename, DataProcessor processor) throws IOException {
        List<Document> docList = shapes.stream().map(Shape::toDocument).collect(Collectors.toList());
        Document root = new Document("shapes", docList);
        byte[] data = root.toJson().getBytes(StandardCharsets.UTF_8);
        if (processor != null) {
            data = processor.processBeforeSave(data);
        }
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(data);
        }
    }

    public List<Shape> deserialize(String filename, DataProcessor processor) throws Exception {
        File file = new File(filename);
        if (!file.exists()) return new ArrayList<>();

        byte[] data = Files.readAllBytes(file.toPath());

        if (processor != null) {
            data = processor.processAfterLoad(data);
        }
        Document root = Document.parse(new String(data, StandardCharsets.UTF_8));
        List<Document> docList = root.getList("shapes", Document.class);

        List<Shape> result = new ArrayList<>();
        if (docList != null) {
            for (Document doc : docList) {
                String type = doc.getString("type");

                ShapeFactory factory = factories.get(type);
                if (factory != null) {
                    Shape shape = factory.create();
                    shape.fromDocument(doc);
                    result.add(shape);
                }
            }
        }
        return result;
    }
}