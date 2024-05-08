package org.hlumak.controller;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;
import org.hlumak.connector.SystemFileConnector;
import org.hlumak.converter.ArticleConverter;
import org.hlumak.service.ArticleService;
import org.hlumak.service.decorator.LoggingArticleServiceDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.List;

public class SAXControllerTest {
    private final ArticleController saxController = new ArticleController(new LoggingArticleServiceDecorator(new ArticleService(new ArticleConverter(), new SystemFileConnector())));

    private SAXControllerTest() {
        saxController.setConvertor("SAX");
    }

    @Test
    public void shouldParseSimpleElement() {
        List<Article> articles = saxController.getAll("src/test/resources/files/Articles");
        Article article = articles.getFirst();
        Assertions.assertEquals(1, article.getId());
        Assertions.assertEquals("New Study Shows Benefits of Exercise", article.getTitle());

        String expectedContent = "A recent study published in the Journal of Health Sciences reveals the numerous benefits of regular exercise, including improved cardiovascular health, increased energy levels, and reduced stress.";
        Assertions.assertEquals(expectedContent, article.getContent());

        Assertions.assertEquals("24.01.2023 14:03", new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime()));

        Assertions.assertEquals(Category.HEALTH, article.getCategory());

        String[] authors = {"John Doe", "Nicolas Spark"};
        Assertions.assertArrayEquals(authors, article.getAuthors().toArray());
    }

    @Test
    public void shouldParseElementWithComment() {
        List<Article> articles = saxController.getAll("src/test/resources/files/Articles");
        Article article = articles.get(1);
        Assertions.assertEquals("Comment1", article.getComment().getText());
    }

    @Test
    public void shouldParseElementWithCommentAndAnswers() {
        List<Article> articles = saxController.getAll("src/test/resources/files/Articles");
        Article article = articles.get(3);
        Comment comment = article.getComment();
        List<String> answers = comment.getAnswers();
        Assertions.assertEquals("Comment3", comment.getText());
        Assertions.assertEquals("Answer1", answers.getFirst());
        Assertions.assertEquals("Answer2", answers.get(1));
    }

    @Test
    public void shouldParseElementWithCyrillicSemicolon() {
        List<Article> articles = saxController.getAll("src/test/resources/files/Articles");
        Article article = articles.get(4);
        Assertions.assertEquals("Researchers анонс a \"major breakthrough\" in cancer research; uncovering a potential new treatment that targets specific cancer cells while minimizing side effects.", article.getContent());
    }
}
