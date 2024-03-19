package org.hlumak.service;

import org.hlumak.bom.Article;
import org.hlumak.connector.CSVConnector;
import org.hlumak.convertor.CSVConvertor;

import java.text.ParseException;
import java.util.List;

public class CSVService implements Service {

    private final CSVConnector csvConnector;

    private final CSVConvertor csvConvertor;

    public CSVService(CSVConnector csvConnector, CSVConvertor csvConvertor) {
        this.csvConnector = csvConnector;
        this.csvConvertor = csvConvertor;
    }

    @Override
    public void createFile(String path, List<Article> articles) {
        csvConnector.write(path, csvConvertor.toDTO(articles));
    }

    @Override
    public List<Article> getAll(String path) throws ParseException {
        return csvConvertor.fromDTO(csvConnector.read(path));
    }
}
