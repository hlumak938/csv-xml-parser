package org.hlumak.converter;

import lombok.Setter;
import org.hlumak.bom.Article;
import org.hlumak.converter.factory.ConverterStrategyFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Component
public class ArticleConverter {

    private ConverterStrategyFactory converter;

    public List<Article> fromDTO(byte[] bytes) {
        return converter.createConverter().fromDTO(bytes);
    }

    public byte[] toDTO(List<Article> articles) {
        return converter.createConverter().toDTO(articles);
    }

    public String getExtension() {
        return converter.createConverter().getExtension();
    }
}
