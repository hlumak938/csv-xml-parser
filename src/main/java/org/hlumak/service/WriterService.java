package org.hlumak.service;

import org.hlumak.entity.Article;

import java.util.ArrayList;

public interface WriterService {
    void writeInFile(String filePath, ArrayList<Article> articles);
}
