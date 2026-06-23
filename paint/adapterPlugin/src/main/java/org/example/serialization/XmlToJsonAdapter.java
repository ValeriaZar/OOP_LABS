package org.example.serialization;

import org.example.plugins.XmlToJsonTransformer;
import java.nio.charset.StandardCharsets;

public class XmlToJsonAdapter implements DataProcessor {

    private final XmlToJsonTransformer transformer = new XmlToJsonTransformer();

    @Override
    public String getName() {
        return "Adapter: " + transformer.getPluginName();
    }

    @Override
    public byte[] processBeforeSave(byte[] data) {
        String json = new String(data, StandardCharsets.UTF_8);
        String xml = transformer.processBeforeSave(json);
        return xml.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public byte[] processAfterLoad(byte[] data) {
        String xml = new String(data, StandardCharsets.UTF_8);
        String json = transformer.processAfterLoad(xml);
        return json.getBytes(StandardCharsets.UTF_8);
    }
}