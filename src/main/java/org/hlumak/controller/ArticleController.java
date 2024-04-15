package org.hlumak.controller;

import lombok.AllArgsConstructor;
import org.hlumak.bom.Article;
import org.hlumak.converter.ConverterStrategy;
import org.hlumak.service.ArticleService;
import org.springframework.stereotype.Controller;

import java.util.List;

@AllArgsConstructor
@Controller
public class ArticleController {

    private final ArticleService service;

    public void createFile(String path, List<Article> articles) {
        service.createFile(path, articles);
    }

    public List<Article> getAll(String path) {
        return service.getAll(path);
    }

    public void setConvertor(ConverterStrategy convertor) {
        service.setConvertor(convertor);
    }
}
