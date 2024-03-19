package org.hlumak.controller;

import org.hlumak.bom.Article;
import org.hlumak.service.DOMService;

import java.text.ParseException;
import java.util.List;

public class DOMController implements Controller {

    private final DOMService domService;

    public DOMController(DOMService domService) {
        this.domService = domService;
    }

    @Override
    public void createFile(String path, List<Article> articles) {
        domService.createFile(path, articles);
    }

    @Override
    public List<Article> getAll(String path) throws ParseException {
        return domService.getAll(path);
    }
}
