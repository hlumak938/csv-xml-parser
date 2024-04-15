package org.hlumak.convertor;

import org.hlumak.bom.Article;

import java.util.List;

public interface ConvertorStrategy {

    byte[] toDTO(List<Article> articles);

    List<Article> fromDTO(byte[] bytes);

    String getExtension();
}
