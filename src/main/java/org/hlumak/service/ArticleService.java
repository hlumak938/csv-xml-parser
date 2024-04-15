package org.hlumak.service;

import lombok.AllArgsConstructor;
import org.hlumak.bom.Article;
import org.hlumak.connector.Connector;
import org.hlumak.converter.ArticleConverter;
import org.hlumak.converter.ConverterStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ArticleService {

    private final ArticleConverter articleConverter;
    private final Connector connector;

    public void createFile(String path, List<Article> articles) {
        connector.write(path + articleConverter.getExtension(), articleConverter.toDTO(articles));
    }

    public List<Article> getAll(String path) {
        return articleConverter.fromDTO(connector.read(path + articleConverter.getExtension()));
    }

    public void setConvertor(ConverterStrategy convertor) {
        articleConverter.setConverter(convertor);
    }
}
