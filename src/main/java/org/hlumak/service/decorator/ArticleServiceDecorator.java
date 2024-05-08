package org.hlumak.service.decorator;

import org.hlumak.bom.Article;

import java.util.List;

public interface ArticleServiceDecorator {
    void createFile(String path, List<Article> articles);

    List<Article> getAll(String path);

    void setConvertor(String strategy);
}
