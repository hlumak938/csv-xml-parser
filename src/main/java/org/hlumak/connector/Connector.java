package org.hlumak.connector;

public interface Connector {
    byte[] read(String path);

    void write(String path, byte[] bytes);
}
