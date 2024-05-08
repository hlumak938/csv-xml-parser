package org.hlumak.service.decorator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hlumak.bom.Article;
import org.hlumak.service.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class LoggingArticleServiceDecorator implements ArticleServiceDecorator{

    private final ArticleService articleService;

    @Override
    public void createFile(String path, List<Article> articles) {
        log.info("Creating file at path: {}", path);
        articleService.createFile(path, articles);
        log.info("File created successfully.");
    }

    @Override
    public List<Article> getAll(String path) {
        log.info("Reading file from path: {}", path);
        List<Article> articles = articleService.getAll(path);
        log.info("File read successfully.");
        return articles;
    }

    @Override
    public void setConvertor(String strategy) {
        articleService.setConvertor(strategy);
    }
}
