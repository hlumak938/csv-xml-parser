package org.hlumak.service;

import lombok.RequiredArgsConstructor;
import org.hlumak.bom.Article;
import org.hlumak.connector.Connector;
import org.hlumak.converter.ArticleConverter;
import org.hlumak.converter.factory.ArticleCSVConverterFactory;
import org.hlumak.converter.factory.ArticleDOMConverterFactory;
import org.hlumak.converter.factory.ArticleSAXConverterFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
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

    public void setConvertor(String strategy) {
        if(strategy.equalsIgnoreCase("DOM")) {
            articleConverter.setConverter(new ArticleDOMConverterFactory());
        } else if(strategy.equalsIgnoreCase("SAX")) {
            articleConverter.setConverter(new ArticleSAXConverterFactory());
        } else if(strategy.equalsIgnoreCase("CSV")) {
            articleConverter.setConverter(new ArticleCSVConverterFactory());
        }
    }
}
