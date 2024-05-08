package org.hlumak.controller;

import lombok.RequiredArgsConstructor;
import org.hlumak.bom.Article;
import org.hlumak.service.decorator.ArticleServiceDecorator;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ArticleController {

    private final ArticleServiceDecorator service;

    public void createFile(String path, List<Article> articles) {
        service.createFile(path, articles);
    }

    public List<Article> getAll(String path) {
        return service.getAll(path);
    }

    public void setConvertor(String strategy) {
        service.setConvertor(strategy);
    }
}
