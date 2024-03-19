package org.hlumak.controller;

import org.hlumak.bom.Article;
import org.hlumak.service.SAXService;

import java.util.List;

public class SAXController implements Controller {

    private final SAXService saxService;

    public SAXController(SAXService saxService) {
        this.saxService = saxService;
    }

    @Override
    public void createFile(String path, List<Article> articles) {
        saxService.createFile(path, articles);
    }

    @Override
    public List<Article> getAll(String path) {
        return saxService.getAll(path);
    }
}
