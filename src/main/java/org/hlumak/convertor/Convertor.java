package org.hlumak.convertor;

import org.hlumak.bom.Article;

import java.text.ParseException;
import java.util.List;

public interface Convertor<D> {

    D toDTO(List<Article> articles);

    List<Article> fromDTO(D dto) throws ParseException;
}
