package org.hlumak.connector;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class SystemFileConnector implements Connector {
    @Override
    public byte[] read(String path) {
        try {
            return Files.readAllBytes(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read file! Path: " + path, e);
        }
    }

    @Override
    public void write(String path, byte[] bytes) {
        try {
            Files.write(Path.of(path), bytes);
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to file! Path: " + path, e);
        }
    }
}
