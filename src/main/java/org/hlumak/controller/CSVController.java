package org.hlumak.controller;

import org.hlumak.bom.Article;
import org.hlumak.service.CSVService;

import java.text.ParseException;
import java.util.List;

public class CSVController implements Controller {

    private final CSVService csvService;

    public CSVController(CSVService csvService) {
        this.csvService = csvService;
    }

    @Override
    public void createFile(String path, List<Article> articles) {
        csvService.createFile(path, articles);
    }

    @Override
    public List<Article> getAll(String path) throws ParseException {
        return csvService.getAll(path);
    }
}
