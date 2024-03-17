package org.hlumak.service;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.hlumak.entity.Article;
import org.hlumak.entity.Comment;
import org.hlumak.entity.Category;

import javax.xml.XMLConstants;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class XMLDOMParserService implements ParserService<List<Element>> {
    @Override
    public ArrayList<Article> parse(List<Element> elements) {
        ArrayList<Article> articles = new ArrayList<>();
        for (Element element : elements) {

            int id = Integer.parseInt(element.getChildText("id"));
            String title = element.getChildText("title");
            String content = element.getChildText("content");

            Date articleTime = null;
            try {
                articleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(element.getChildText("time"));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            Category category = Category.valueOf(element.getChildText("category"));

            List<Element> authorsXML = element.getChild("authors").getChildren("author");
            List<String> authors = new ArrayList<>();
            if(authorsXML != null) {
                for (Element authorsElement : authorsXML) {
                    authors.add(authorsElement.getText());
                }
            }

            Element commentElement = element.getChild("comment");
            Comment comment = null;
            if(commentElement != null) {
                List<Element> answersElements = commentElement.getChild("answers") != null ? commentElement.getChild("answers").getChildren("answer") : null;
                String commentText = commentElement.getChildText("text");
                if(answersElements == null) {
                    comment = new Comment(commentText);
                } else {
                    List<String> answers = new ArrayList<>();
                    for(Element answersElement : answersElements) {
                        answers.add(answersElement.getText());
                    }
                    comment = new Comment(commentText, answers);
                }
            }
            articles.add(new Article(id, title, content, articleTime, category, authors, comment));
        }
        return articles;
    }

    @Override
    public List<Element> readFromFile(String filePath) {
        SAXBuilder sax = new SAXBuilder();

        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        sax.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        Document doc = null;
        try {
            doc = sax.build(new File(filePath));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return doc != null ? doc.getRootElement().getChildren("article") : null;
    }

    @Override
    public void writeInFile(String filePath, ArrayList<Article> articles) {
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
            for(String author : article.getAuthors()) {
                authorElement.addContent(new Element("author").setText(author));
            }

            Comment comment = article.getComment();
            if(comment != null) {
                Element commentElement = new Element("comment");
                articleElement.addContent(commentElement);
                commentElement.addContent(new Element("text").setText(comment.getText()));

                List<String> answers = comment.getAnswers();
                if(answers != null) {
                    Element answerElement = new Element("answers");
                    commentElement.addContent(answerElement);
                    for (String answer : answers) {
                        answerElement.addContent(new Element("answer").setText(answer));
                    }
                }
            }

            // append child to root
            doc.getRootElement().addContent(articleElement);
        }
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.setFormat(Format.getPrettyFormat());

        try(FileWriter fileWriter = new FileWriter(filePath)){
            xmlOutputter.output(doc, fileWriter);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }
}
