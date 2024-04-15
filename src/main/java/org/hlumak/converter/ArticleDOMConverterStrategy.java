package org.hlumak.converter;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javax.xml.XMLConstants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ArticleDOMConverterStrategy implements ConverterStrategy {

    private final SAXBuilder sax = instantiateSaxBuilder();

    private static SAXBuilder instantiateSaxBuilder() {
        SAXBuilder sax = new SAXBuilder();
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        return sax;
    }

    @Override
    public byte[] toDTO(List<Article> articles) {
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
                if (!answers.isEmpty()) {
                    Element answerElement = new Element("answers");
                    commentElement.addContent(answerElement);
                    for (String answer : answers) {
                        answerElement.addContent(new Element("answer").setText(answer));
                    }
                }
            }

            doc.getRootElement().addContent(articleElement);
        }

        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        return xmlOutputter.outputString(doc).getBytes();
    }

    @Override
    public List<Article> fromDTO(byte[] bytes) {
        try {
            Document doc = sax.build(new ByteArrayInputStream(bytes));
            ArrayList<Article> result = new ArrayList<>();

            for (Element element : doc.getRootElement().getChildren("article")) {
                Article article = new Article(Integer.parseInt(element.getChildText("id")));
                article.setTitle(element.getChildText("title"));
                article.setContent(element.getChildText("content"));
                article.setTime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(element.getChildText("time")));
                article.setCategory(Category.valueOf(element.getChildText("category")));

                List<Element> authorsXML = element.getChild("authors").getChildren("author");
                if (authorsXML != null) {
                    article.setAuthors(new ArrayList<>());
                    for (Element authorsElement : authorsXML) {
                        article.getAuthors().add(authorsElement.getText());
                    }
                }
                Element commentElement = element.getChild("comment");
                if (commentElement != null) {
                    article.setComment(new Comment(commentElement.getChildText("text")));
                    if (commentElement.getChild("answers") != null) {
                        List<Element> answersElements = commentElement.getChild("answers").getChildren("answer");
                        for (Element answersElement : answersElements) {
                            article.getComment().getAnswers().add(answersElement.getText());
                        }
                    }
                }
                result.add(article);
            }
            return result;
        } catch (JDOMException | IOException | ParseException e) {
            throw new RuntimeException("Couldn't parse bytes!", e);
        }
    }

    @Override
    public String getExtension() {
        return ".xml";
    }

}
