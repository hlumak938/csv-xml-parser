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
import java.util.Date;
import java.util.List;

public class MapArticleObjectHandlerSax extends DefaultHandler {
    private final StringBuilder currentValue = new StringBuilder();
    @Getter
    private List<Article> result;
    private Article currentArticle;
    private Comment currentComment;
    private List<String> currentAuthors;
    private List<String> currentAnswers;

    @Override
    public void startDocument() {
        result = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {

        currentValue.setLength(0);

        if (qName.equalsIgnoreCase("article")) {
            currentArticle = new Article();
        }

        if (qName.equalsIgnoreCase("comment")) {
            currentComment = new Comment();
        }

        if (qName.equalsIgnoreCase("authors")) {
            currentAuthors = new ArrayList<>();
        }

        if (qName.equalsIgnoreCase("answers")) {
            currentAnswers = new ArrayList<>();
        }

    }

    public void endElement(String uri, String localName, String qName) {

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
            Date time = null;
            try {
                time = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(currentValue.toString());
            } catch (ParseException e) {
                System.out.println(e.getMessage());
            }
            currentArticle.setTime(time);
        }

        if (qName.equalsIgnoreCase("category")) {
            currentArticle.setCategory(Category.valueOf(currentValue.toString()));
        }

        if (qName.equalsIgnoreCase("author")) {
            currentAuthors.add(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("authors")) {
            currentArticle.setAuthors(currentAuthors);
        }

        if (qName.equalsIgnoreCase("comment")) {
            currentArticle.setComment(currentComment);
        }

        if (qName.equalsIgnoreCase("text")) {
            currentComment.setText(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("answers")) {
            currentComment.setAnswers(currentAnswers);
        }

        if (qName.equalsIgnoreCase("answer")) {
            currentAnswers.add(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("article")) {
            result.add(currentArticle);
        }

    }

    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }
}
