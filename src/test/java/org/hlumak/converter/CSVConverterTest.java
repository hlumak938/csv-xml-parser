package org.hlumak.converter;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;
import org.hlumak.converter.strategy.ArticleCSVConverterStrategy;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVConverterTest {

    private final ArticleCSVConverterStrategy csvConvertor = new ArticleCSVConverterStrategy();

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

        byte[] resultBytes = csvConvertor.toDTO(articles);
        String expectedString = "id;title;content;time;category;authors;comment;answers\n" +
                "1;Sample Title;Sample Content;" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime()) + ";Science;Nicolas,Mikita;;\n";
        byte[] expectedBytes = expectedString.getBytes();

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

        byte[] resultBytes = csvConvertor.toDTO(articles);
        String expectedString = "id;title;content;time;category;authors;comment;answers\n" +
                "1;Sample Title;Sample Content;" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime()) + ";Science;Nicolas,Mikita;Some Comment;Some Answer,Some Random Answer\n";
        byte[] expectedBytes = expectedString.getBytes();

        assertArrayEquals(expectedBytes, resultBytes);
    }

    @Test
    public void shouldReturnArticleWithSpecifiedCharacters() {
        Article article = new Article(1);

        Comment comment = new Comment();
        comment.setText("Some Comment");
        List<String> answers = new ArrayList<>();
        answers.add("Some Answer");
        answers.add("Some Random Answer");

        comment.setAnswers(answers);

        article.setTitle("Sample Title");
        article.setContent("Researchers анонс a \"major breakthrough\" in cancer research; uncovering a potential");
        article.setTime(new Date());
        article.setCategory(Category.SCIENCE);
        article.setAuthors(List.of("Nicolas", "Mikita"));
        article.setComment(comment);
        article.getComment().setText(comment.getText());
        article.getComment().setAnswers(comment.getAnswers());

        List<Article> articles = new ArrayList<>();
        articles.add(article);

        byte[] resultBytes = csvConvertor.toDTO(articles);
        String expectedString = "id;title;content;time;category;authors;comment;answers\n" +
                "1;Sample Title;\"Researchers анонс a \"\"major breakthrough\"\" in cancer research; uncovering a potential\";" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime()) + ";Science;Nicolas,Mikita;Some Comment;Some Answer,Some Random Answer\n";
        byte[] expectedBytes = expectedString.getBytes();

        assertArrayEquals(expectedBytes, resultBytes);
    }

    @Test
    public void shoudlBeEmptyByteArray() {
        byte[] emptyBytes = new byte[0];
        List<Article> resultArticles = csvConvertor.fromDTO(emptyBytes);

        assertEquals(0, resultArticles.size());
    }

    @Test
    public void shouldBeSingleArticleWithoutComments() {
        String data = "id;title;content;time;category;authors;comment;answers\r\n" +
                "1;Sample Title;Sample Content;01.01.2009 01:20;SCIENCE;Author1,Author2;;";

        byte[] bytes = data.getBytes();

        List<Article> resultArticles = csvConvertor.fromDTO(bytes);

        assertEquals(1, resultArticles.size());

        Article resultArticle = resultArticles.getFirst();

        assertEquals(1, resultArticle.getId());
        assertEquals("Sample Title", resultArticle.getTitle());
        assertEquals("Sample Content", resultArticle.getContent());
        assertEquals(new Date(1230765600000L), resultArticle.getTime());
        assertEquals(Category.SCIENCE, resultArticle.getCategory());
        assertEquals(List.of("Author1", "Author2"), resultArticle.getAuthors());
        assertNull(resultArticle.getComment());
    }

    @Test
    public void shouldBeSingleArticle() {
        String data = "id;title;content;time;category;authors;comment;answers\r\n" +
                "1;Sample Title;Sample Content;01.01.2009 01:20;SCIENCE;Author1,Author2;Some Comment;Some Answer, Some Random Answer";

        byte[] bytes = data.getBytes();

        List<Article> resultArticles = csvConvertor.fromDTO(bytes);

        assertEquals(1, resultArticles.size());

        Article resultArticle = resultArticles.getFirst();

        assertEquals(1, resultArticle.getId());
        assertEquals("Sample Title", resultArticle.getTitle());
        assertEquals("Sample Content", resultArticle.getContent());
        assertEquals(new Date(1230765600000L), resultArticle.getTime());
        assertEquals(Category.SCIENCE, resultArticle.getCategory());
        assertEquals(List.of("Author1", "Author2"), resultArticle.getAuthors());
        assertEquals("Some Comment", resultArticle.getComment().getText());
        assertEquals(List.of("Some Answer", " Some Random Answer"), resultArticle.getComment().getAnswers());
    }

    @Test
    public void shouldBeSingleArticleWithSpecifiedCharacters() {
        String data = "id;title;content;time;category;authors;comment;answers\r\n" +
                "1;Sample Title;\"Researchers анонс a \"\"major breakthrough\"\" in cancer research; uncovering a potential\";01.01.2009 01:20;SCIENCE;Author1,Author2;Some Comment;Some Answer, Some Random Answer";

        byte[] bytes = data.getBytes();

        List<Article> resultArticles = csvConvertor.fromDTO(bytes);

        assertEquals(1, resultArticles.size());

        Article resultArticle = resultArticles.getFirst();

        assertEquals(1, resultArticle.getId());
        assertEquals("Sample Title", resultArticle.getTitle());
        assertEquals("Researchers анонс a \"major breakthrough\" in cancer research; uncovering a potential", resultArticle.getContent());
        assertEquals(new Date(1230765600000L), resultArticle.getTime());
        assertEquals(Category.SCIENCE, resultArticle.getCategory());
        assertEquals(List.of("Author1", "Author2"), resultArticle.getAuthors());
        assertEquals("Some Comment", resultArticle.getComment().getText());
        assertEquals(List.of("Some Answer", " Some Random Answer"), resultArticle.getComment().getAnswers());
    }
}
