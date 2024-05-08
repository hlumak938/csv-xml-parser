package org.hlumak.converter.strategy;

import org.hlumak.bom.Article;

import java.util.List;

public interface ConverterStrategy {

    byte[] toDTO(List<Article> articles);

    List<Article> fromDTO(byte[] bytes);

    String getExtension();
}
