package org.example.plugins;

import org.bson.Document;
import org.bson.json.JsonWriterSettings;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.InputSource;

public class XmlToJsonTransformer {

    public String processBeforeSave(String jsonData) {
        if (jsonData == null || jsonData.trim().isEmpty()) {
            return "<Shapes></Shapes>";
        }

        Document root = Document.parse(jsonData);
        List<Document> shapes = root.getList("shapes", Document.class);

        StringBuilder xml = new StringBuilder("<Shapes>");

        if (shapes != null) {
            for (Document shape : shapes) {
                String type = shape.getString("type");

                xml.append("<Shape Type=\"").append(type).append("\">");

                for (String key : shape.keySet()) {
                    if (key.equals("type")) continue;

                    Object value = shape.get(key);
                    xml.append("<").append(key).append(">")
                            .append(value)
                            .append("</").append(key).append(">");
                }

                xml.append("</Shape>");
            }
        }

        xml.append("</Shapes>");
        return xml.toString();
    }

    public String processAfterLoad(String xmlData) {
        try {
            if (xmlData == null || xmlData.trim().isEmpty()) {
                return "{\"shapes\":[]}";
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            org.w3c.dom.Document xmlDoc = builder.parse(
                    new InputSource(new StringReader(xmlData))
            );

            NodeList shapeNodes = xmlDoc.getElementsByTagName("Shape");

            List<Document> shapes = new ArrayList<>();

            for (int i = 0; i < shapeNodes.getLength(); i++) {
                Element shapeEl = (Element) shapeNodes.item(i);

                String type = shapeEl.getAttribute("Type");

                Document doc = new Document();
                doc.append("type", type);

                NodeList children = shapeEl.getChildNodes();

                for (int j = 0; j < children.getLength(); j++) {
                    if (children.item(j) instanceof Element) {
                        Element param = (Element) children.item(j);

                        String key = param.getTagName();
                        String value = param.getTextContent();

                        if (value != null && !value.isEmpty()) {
                            try {
                                doc.append(key, Double.parseDouble(value));
                            } catch (Exception e) {
                                doc.append(key, value);
                            }
                        }
                    }
                }

                shapes.add(doc);
            }

            Document root = new Document("shapes", shapes);

            return root.toJson(JsonWriterSettings.builder().build());

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"shapes\":[]}";
        }
    }

    public String getPluginName() {
        return "XML-JSON";
    }
}