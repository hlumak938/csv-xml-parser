package org.hlumak.connector;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class SystemFileConnectorTest {
    @Test
    public void testReadFile() throws IOException {
        SystemFileConnector fileConnector = new SystemFileConnector();

        Path testFilePath = Path.of("testFile.txt");
        String testContent = "Test content";

        Files.write(testFilePath, testContent.getBytes());

        byte[] resultBytes = fileConnector.read(String.valueOf(testFilePath));

        assertArrayEquals(testContent.getBytes(), resultBytes);

        Files.deleteIfExists(testFilePath);
    }

    @Test
    public void testWriteFile() throws IOException {
        SystemFileConnector fileConnector = new SystemFileConnector();

        Path testFilePath = Path.of("testFile.txt");
        byte[] testBytes = "Test content".getBytes();

        fileConnector.write(String.valueOf(testFilePath), testBytes);

        byte[] resultBytes = Files.readAllBytes(testFilePath);

        assertArrayEquals(testBytes, resultBytes);

        Files.deleteIfExists(testFilePath);
    }
}
