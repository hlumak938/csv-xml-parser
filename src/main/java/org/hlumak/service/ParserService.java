package org.hlumak.service;

import org.hlumak.entity.Article;

import java.util.ArrayList;

public interface ParserService<T> {

    ArrayList<Article> parse(T obj);

    T readFromFile(String filePath);
}
