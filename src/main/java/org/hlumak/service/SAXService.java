package org.hlumak.service;

import org.hlumak.bom.Article;
import org.hlumak.connector.SAXConnector;

import java.util.List;

public class SAXService implements Service {

    private final SAXConnector saxConnector;

    public SAXService(SAXConnector saxConnector) {
        this.saxConnector = saxConnector;
    }

    @Override
    public void createFile(String path, List<Article> articles) {
        saxConnector.write(path, articles);
    }

    @Override
    public List<Article> getAll(String path) {
        return saxConnector.read(path);
    }
}
