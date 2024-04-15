package org.hlumak.service;

import lombok.AllArgsConstructor;
import org.hlumak.bom.Article;
import org.hlumak.connector.Connector;
import org.hlumak.convertor.ArticleConvertor;
import org.hlumak.convertor.ConvertorStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ArticleService {

    private final ArticleConvertor articleConvertor;
    private final Connector connector;

    public void createFile(String path, List<Article> articles) {
        connector.write(path + articleConvertor.getExtension(), articleConvertor.toDTO(articles));
    }

    public List<Article> getAll(String path) {
        return articleConvertor.fromDTO(connector.read(path + articleConvertor.getExtension()));
    }

    public void setConvertor(ConvertorStrategy convertor) {
        articleConvertor.setConvertor(convertor);
    }
}
