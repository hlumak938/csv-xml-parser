package org.hlumak.controller;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;
import org.hlumak.connector.CSVConnector;
import org.hlumak.convertor.CSVConvertor;
import org.hlumak.service.CSVService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CSVControllerTest {

    private final CSVController csvController = new CSVController(new CSVService(new CSVConnector(), new CSVConvertor()));

    private final List<Article> articles = csvController.getAll("src/test/resources/files/Articles.csv");

    public CSVControllerTest() throws ParseException {
    }


    @Test
    public void shouldParseSimpleRow() {
        Article article = articles.get(0);
        Assertions.assertEquals(1, article.getId());
        Assertions.assertEquals("New Study Shows Benefits of Exercise", article.getTitle());

        String expectedContent = "A recent study published in the Journal of Health Sciences reveals the numerous benefits of regular exercise, including improved cardiovascular health, increased energy levels, and reduced stress.";
        Assertions.assertEquals(expectedContent, article.getContent());

        String time = "24.01.2023  14:03:00";
        Date articleTime = null;
        try {
            articleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(time);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Assertions.assertEquals(articleTime, article.getTime());

        Assertions.assertEquals(Category.HEALTH, article.getCategory());

        String[] authors = {"John Doe", " Nicolas Spark"};
        Assertions.assertArrayEquals(authors, article.getAuthors().toArray());
    }

    @Test
    public void shouldParseRowWithComment() {
        Article article = articles.get(1);
        Assertions.assertEquals("Comment1", article.getComment().getText());
    }

    @Test
    public void shouldParseRowWithCommentAndAnswers() {
        Article article = articles.get(3);
        Assertions.assertEquals(new Comment("Comment3", Arrays.asList("Answer1", " Answer2")), article.getComment());
    }

    @Test
    public void shouldParseRowWithCyrillicSemicolon() {
        Article article = articles.get(4);
        String expected = "Researchers анонс a \"major breakthrough\" in cancer research; uncovering a potential new treatment that targets specific cancer cells while minimizing side effects.";
        Assertions.assertEquals(expected, article.getContent());
    }
}
