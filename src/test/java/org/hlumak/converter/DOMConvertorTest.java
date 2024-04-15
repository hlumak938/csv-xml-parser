package org.hlumak.converter;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;
import org.hlumak.convertor.ArticleDOMConvertorStrategy;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DOMConvertorTest {

    private final ArticleDOMConvertorStrategy domConvertor = new ArticleDOMConvertorStrategy();

    @Test
    public void shouldReturnArticleWithoutCommentsAndAnswers() {
        Article article = new Article(1);

        article.setTitle("Sample Title");
        article.setContent("Sample Content");
        article.setTime(new Date());
        article.setCategory(Category.SCIENCE);
        article.setAuthors(List.of("Nicolas", "Mikita"));

        List<Article> articles = new ArrayList<>();
        articles.add(article);

        Document doc = new Document();
        doc.setRootElement(new Element("news"));
        Element articleElement = new Element("article");

        articleElement.addContent(new Element("id").setText(Integer.toString(article.getId())));
        articleElement.addContent(new Element("title").setText(article.getTitle()));
        articleElement.addContent(new Element("content").setText(article.getContent()));
        articleElement.addContent(new Element("time").setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime())));
        articleElement.addContent(new Element("category").setText(article.getCategory().toString()));

        Element authorElement = new Element("authors");
        articleElement.addContent(authorElement);
        for (String author : article.getAuthors()) {
            authorElement.addContent(new Element("author").setText(author));
        }

        doc.getRootElement().addContent(articleElement);

        byte[] resultBytes = domConvertor.toDTO(articles);

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        byte[] expectedBytes = xmlOutputter.outputString(doc).getBytes();

        assertArrayEquals(expectedBytes, resultBytes);
    }

    @Test
    public void shouldReturnFullArticle() {
        Article article = new Article(1);

        Comment comment = new Comment();
        comment.setText("Some Comment");
        List<String> answers = new ArrayList<>();
        answers.add("Some Answer");
        answers.add("Some Random Answer");

        comment.setAnswers(answers);

        article.setTitle("Sample Title");
        article.setContent("Sample Content");
        article.setTime(new Date());
        article.setCategory(Category.SCIENCE);
        article.setAuthors(List.of("Nicolas", "Mikita"));
        article.setComment(comment);
        article.getComment().setText(comment.getText());
        article.getComment().setAnswers(comment.getAnswers());

        List<Article> articles = new ArrayList<>();
        articles.add(article);

        Document doc = new Document();
        doc.setRootElement(new Element("news"));
        Element articleElement = new Element("article");

        articleElement.addContent(new Element("id").setText(Integer.toString(article.getId())));
        articleElement.addContent(new Element("title").setText(article.getTitle()));
        articleElement.addContent(new Element("content").setText(article.getContent()));
        articleElement.addContent(new Element("time").setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime())));
        articleElement.addContent(new Element("category").setText(article.getCategory().toString()));

        Element authorElement = new Element("authors");
        articleElement.addContent(authorElement);
        for (String author : article.getAuthors()) {
            authorElement.addContent(new Element("author").setText(author));
        }

        Element commentElement = new Element("comment");
        articleElement.addContent(commentElement);
        commentElement.addContent(new Element("text").setText(comment.getText()));

        Element answerElement = new Element("answers");
        commentElement.addContent(answerElement);
        for (String answer : answers) {
            answerElement.addContent(new Element("answer").setText(answer));
        }

        doc.getRootElement().addContent(articleElement);

        byte[] resultBytes = domConvertor.toDTO(articles);
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        byte[] expectedBytes = xmlOutputter.outputString(doc).getBytes();

        assertArrayEquals(expectedBytes, resultBytes);
    }

    @Test
    public void shouldBeSingleArticleWithoutComments() {
        Article article = new Article(1);

        article.setTitle("Sample Title");
        article.setContent("Sample Content");
        article.setTime(new Date(1230765600000L));
        article.setCategory(Category.SCIENCE);
        article.setAuthors(List.of("Nicolas", "Mikita"));

        Document doc = new Document();
        doc.setRootElement(new Element("news"));
        Element articleElement = new Element("article");

        articleElement.addContent(new Element("id").setText(Integer.toString(article.getId())));
        articleElement.addContent(new Element("title").setText(article.getTitle()));
        articleElement.addContent(new Element("content").setText(article.getContent()));
        articleElement.addContent(new Element("time").setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime())));
        articleElement.addContent(new Element("category").setText(article.getCategory().toString()));

        Element authorElement = new Element("authors");
        articleElement.addContent(authorElement);
        for (String author : article.getAuthors()) {
            authorElement.addContent(new Element("author").setText(author));
        }

        doc.getRootElement().addContent(articleElement);

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        byte[] bytes = xmlOutputter.outputString(doc).getBytes();

        List<Article> resultArticles = domConvertor.fromDTO(bytes);

        assertEquals(1, resultArticles.size());

        Article resultArticle = resultArticles.getFirst();

        assertEquals(1, resultArticle.getId());
        assertEquals("Sample Title", resultArticle.getTitle());
        assertEquals("Sample Content", resultArticle.getContent());
        assertEquals(new Date(1230765600000L), resultArticle.getTime());
        assertEquals(Category.SCIENCE, resultArticle.getCategory());
        assertEquals(List.of("Nicolas", "Mikita"), resultArticle.getAuthors());
        assertNull(resultArticle.getComment());
    }

    @Test
    public void shouldBeSingleArticle() {
        Article article = new Article(1);

        Comment comment = new Comment();
        comment.setText("Some Comment");
        List<String> answers = new ArrayList<>();
        answers.add("Some Answer");
        answers.add("Some Random Answer");

        comment.setAnswers(answers);

        article.setTitle("Sample Title");
        article.setContent("Sample Content");
        article.setTime(new Date(1230765600000L));
        article.setCategory(Category.SCIENCE);
        article.setAuthors(List.of("Nicolas", "Mikita"));
        article.setComment(comment);
        article.getComment().setText(comment.getText());
        article.getComment().setAnswers(comment.getAnswers());

        Document doc = new Document();
        doc.setRootElement(new Element("news"));
        Element articleElement = new Element("article");

        articleElement.addContent(new Element("id").setText(Integer.toString(article.getId())));
        articleElement.addContent(new Element("title").setText(article.getTitle()));
        articleElement.addContent(new Element("content").setText(article.getContent()));
        articleElement.addContent(new Element("time").setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime())));
        articleElement.addContent(new Element("category").setText(article.getCategory().toString()));

        Element authorElement = new Element("authors");
        articleElement.addContent(authorElement);
        for (String author : article.getAuthors()) {
            authorElement.addContent(new Element("author").setText(author));
        }

        Element commentElement = new Element("comment");
        articleElement.addContent(commentElement);
        commentElement.addContent(new Element("text").setText(comment.getText()));

        Element answerElement = new Element("answers");
        commentElement.addContent(answerElement);
        for (String answer : answers) {
            answerElement.addContent(new Element("answer").setText(answer));
        }

        doc.getRootElement().addContent(articleElement);

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        byte[] bytes = xmlOutputter.outputString(doc).getBytes();

        List<Article> resultArticles = domConvertor.fromDTO(bytes);

        assertEquals(1, resultArticles.size());

        Article resultArticle = resultArticles.getFirst();

        assertEquals(1, resultArticle.getId());
        assertEquals("Sample Title", resultArticle.getTitle());
        assertEquals("Sample Content", resultArticle.getContent());
        assertEquals(new Date(1230765600000L), resultArticle.getTime());
        assertEquals(Category.SCIENCE, resultArticle.getCategory());
        assertEquals(List.of("Nicolas", "Mikita"), resultArticle.getAuthors());
        assertEquals("Some Comment", resultArticle.getComment().getText());
        assertEquals(List.of("Some Answer", "Some Random Answer"), resultArticle.getComment().getAnswers());
    }

}
