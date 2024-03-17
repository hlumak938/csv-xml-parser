package org.hlumak.services;

import org.hlumak.entity.Article;
import org.hlumak.entity.Category;
import org.hlumak.service.MapArticleObjectHandlerSax;
import org.hlumak.service.XMLSAXParserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class XMLSAXParserServiceTest {
    private final XMLSAXParserService xmlSAXParserService = new XMLSAXParserService();
    private final MapArticleObjectHandlerSax mapArticleObjectHandlerSax = xmlSAXParserService.readFromFile("files/Data_About_Articles.xml");
    private final ArrayList<Article> result = xmlSAXParserService.parse(mapArticleObjectHandlerSax);

    @Test
    public void shouldParseSimpleElement() {
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
}
