package org.hlumak.controller;

import org.hlumak.bom.Article;

import java.text.ParseException;
import java.util.List;

public interface Controller {
    void createFile(String path, List<Article> articles);

    List<Article> getAll(String path) throws ParseException;
}
