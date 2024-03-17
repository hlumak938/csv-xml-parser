package org.hlumak.services;

import org.hlumak.entity.Article;
import org.hlumak.entity.Category;
import org.hlumak.entity.Comment;
import org.hlumak.service.XMLDOMParserService;
import org.jdom2.Element;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class XMLDOMParserServiceTest {
    private final XMLDOMParserService xmlParser = new XMLDOMParserService();

    private final List<Element> parseString = xmlParser.readFromFile("files/Data_About_Articles.xml");
    private final ArrayList<Article> result = xmlParser.parse(parseString);

    @Test
    public void shouldParseSimpleRow() {
        Article article = result.get(0);
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

        String[] authors = {"John Doe", "Nicolas Spark"};
        Assertions.assertArrayEquals(authors, article.getAuthors().toArray());
    }

    @Test
    public void shouldParseRowWithComment() {
        Article article = result.get(1);
        Assertions.assertEquals("Comment1", article.getComment().getText());
    }

    @Test
    public void shouldParseRowWithCommentAndAnswers() {
        Article article = result.get(3);
        Assertions.assertEquals(new Comment("Comment3", Arrays.asList("Answer1", "Answer2")), article.getComment());
    }

    @Test
    public void shouldParseRowWithCyrillicSemicolon() {
        Article article = result.get(4);
        String expected = "Researchers анонс a \"major breakthrough\" in cancer research; uncovering a potential new treatment that targets specific cancer cells while minimizing side effects.";
        Assertions.assertEquals(expected, article.getContent());
    }
}
