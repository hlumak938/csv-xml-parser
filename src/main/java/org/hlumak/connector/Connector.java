package org.hlumak.connector;

public interface Connector<D> {
    D read(String path);

    void write(String path, D dto);
}
