package org.hlumak.converter;

import lombok.Setter;
import org.hlumak.bom.Article;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Component
public class ArticleConverter {

    private ConverterStrategy converter;

    public ArticleConverter() {
        converter = new ArticleCSVConverterStrategy();
    }

    public List<Article> fromDTO(byte[] bytes) {
        return converter.fromDTO(bytes);
    }

    public byte[] toDTO(List<Article> articles) {
        return converter.toDTO(articles);
    }

    public String getExtension() {
        return converter.getExtension();
    }
}
