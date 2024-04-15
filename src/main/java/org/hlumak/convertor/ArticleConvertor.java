package org.hlumak.convertor;

import lombok.Setter;
import org.hlumak.bom.Article;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Component
public class ArticleConvertor {

    private ConvertorStrategy convertor;

    public ArticleConvertor() {
        convertor = new ArticleCSVConvertorStrategy();
    }

    public List<Article> fromDTO(byte[] bytes) {
        return convertor.fromDTO(bytes);
    }

    public byte[] toDTO(List<Article> articles) {
        return convertor.toDTO(articles);
    }

    public String getExtension() {
        return convertor.getExtension();
    }
}
