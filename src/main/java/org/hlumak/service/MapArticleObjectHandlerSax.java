package org.hlumak.service;

import lombok.Getter;
import org.hlumak.entity.Article;
import org.hlumak.entity.Comment;
import org.hlumak.entity.Category;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MapArticleObjectHandlerSax extends DefaultHandler {
    private final StringBuilder currentValue = new StringBuilder();
    @Getter
    List<Article> result;
    Article currentArticle;
    Comment currentComment;

    @Override
    public void startDocument() {
        result = new ArrayList<>();
    }

    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) {

        // reset the tag value
        currentValue.setLength(0);

        // start of loop
        if (qName.equalsIgnoreCase("article")) {
            // new article
            currentArticle = new Article();
        }

        if (qName.equalsIgnoreCase("comment")) {
            // new article
            currentComment = new Comment();
        }

    }

    public void endElement(String uri,
                           String localName,
                           String qName) {

        if (qName.equalsIgnoreCase("id")) {
            currentArticle.setId(Integer.parseInt(currentValue.toString()));
        }

        if (qName.equalsIgnoreCase("title")) {
            currentArticle.setTitle(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("content")) {
            currentArticle.setContent(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("time")) {
            Date time;
            try {
                time = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(currentValue.toString());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            currentArticle.setTime(time);
        }

        if (qName.equalsIgnoreCase("category")) {
            currentArticle.setCategory(Category.valueOf(currentValue.toString()));
        }

        if (qName.equalsIgnoreCase("authors")) {
            currentArticle.setAuthors(Collections.singletonList(currentValue.toString()));
        }

        currentArticle.setComment(null);

        // end of loop
        if (qName.equalsIgnoreCase("article")) {
            result.add(currentArticle);
        }

    }

    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }
}
