package org.hlumak.convertor;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;
import org.hlumak.dto.DOMDTO;
import org.jdom2.Document;
import org.jdom2.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DOMConvertor implements Convertor<DOMDTO> {
    @Override
    public DOMDTO toDTO(List<Article> articles) {
        Document doc = new Document();
        doc.setRootElement(new Element("news"));

        for (Article article : articles) {
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

            Comment comment = article.getComment();
            if (comment != null) {
                Element commentElement = new Element("comment");
                articleElement.addContent(commentElement);
                commentElement.addContent(new Element("text").setText(comment.getText()));

                List<String> answers = comment.getAnswers();
                if (answers != null) {
                    Element answerElement = new Element("answers");
                    commentElement.addContent(answerElement);
                    for (String answer : answers) {
                        answerElement.addContent(new Element("answer").setText(answer));
                    }
                }
            }

            doc.getRootElement().addContent(articleElement);
        }
        return new DOMDTO(doc);
    }

    @Override
    public List<Article> fromDTO(DOMDTO dto) throws ParseException {
        ArrayList<Article> articles = new ArrayList<>();
        for (Element element : dto.getDocument().getRootElement().getChildren("article")) {

            int id = Integer.parseInt(element.getChildText("id"));
            String title = element.getChildText("title");
            String content = element.getChildText("content");

            Date articleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(element.getChildText("time"));

            Category category = Category.valueOf(element.getChildText("category"));

            List<Element> authorsXML = element.getChild("authors").getChildren("author");
            List<String> authors = new ArrayList<>();
            if (authorsXML != null) {
                for (Element authorsElement : authorsXML) {
                    authors.add(authorsElement.getText());
                }
            }

            Element commentElement = element.getChild("comment");
            Comment comment = null;
            if (commentElement != null) {
                List<Element> answersElements = commentElement.getChild("answers") != null ? commentElement.getChild("answers").getChildren("answer") : null;
                String commentText = commentElement.getChildText("text");
                if (answersElements == null) {
                    comment = new Comment(commentText);
                } else {
                    List<String> answers = new ArrayList<>();
                    for (Element answersElement : answersElements) {
                        answers.add(answersElement.getText());
                    }
                    comment = new Comment(commentText, answers);
                }
            }
            articles.add(new Article(id, title, content, articleTime, category, authors, comment));
        }
        return articles;
    }
}
