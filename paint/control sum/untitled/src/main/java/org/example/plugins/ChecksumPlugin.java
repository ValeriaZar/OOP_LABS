package org.example.plugins;

import org.example.serialization.DataProcessor;
import java.security.MessageDigest;
import java.util.Arrays;

public class ChecksumPlugin implements DataProcessor {
    @Override
    public String getName() {
        return "SHA-256 Checksum";
    }

    @Override
    public byte[] processBeforeSave(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(data);

            byte[] result = new byte[data.length + hash.length];
            System.arraycopy(data, 0, result, 0, data.length);
            System.arraycopy(hash, 0, result, data.length, hash.length);
            return result;
        } catch (Exception e) {
            return data;
        }
    }

    @Override
    public byte[] processAfterLoad(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        int hashSize = md.getDigestLength();

        if (data.length < hashSize) throw new Exception("Data is too short to contain a hash");

        int dataLength = data.length - hashSize;
        byte[] originalData = Arrays.copyOfRange(data, 0, dataLength);
        byte[] savedHash = Arrays.copyOfRange(data, dataLength, data.length);

        byte[] calculatedHash = md.digest(originalData);
        if (!Arrays.equals(savedHash, calculatedHash)) {
            throw new Exception("CRITICAL: Checksum mismatch! Data is corrupted or tampered with.");
        }

        return originalData;
    }
}