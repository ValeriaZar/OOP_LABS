package org.example.serialization;

public interface DataProcessor {
    String getName();
    byte[] processBeforeSave(byte[] data);
    byte[] processAfterLoad(byte[] data) throws Exception;
}
