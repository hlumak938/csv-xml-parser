package org.hlumak.service;

import org.hlumak.bom.Article;
import org.hlumak.connector.DOMConnector;
import org.hlumak.convertor.DOMConvertor;

import java.text.ParseException;
import java.util.List;

public class DOMService implements Service {

    private final DOMConnector domConnector;

    private final DOMConvertor domConvertor;

    public DOMService(DOMConnector domConnector, DOMConvertor domConvertor) {
        this.domConnector = domConnector;
        this.domConvertor = domConvertor;
    }

    @Override
    public void createFile(String path, List<Article> articles) {
        domConnector.write(path, domConvertor.toDTO(articles));
    }

    @Override
    public List<Article> getAll(String path) throws ParseException {
        return domConvertor.fromDTO(domConnector.read(path));
    }
}
