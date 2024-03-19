package org.hlumak.connector;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SAXConnector implements Connector<List<Article>> {

    @Override
    public List<Article> read(String path) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        List<Article> articles = new ArrayList<>();

        try {
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new FileInputStream(path));

            Article article = null;
            List<String> authors = null;
            Comment comment = null;
            List<String> answers = null;

            while (reader.hasNext()) {

                int eventType = reader.next();

                if (eventType == XMLEvent.START_ELEMENT) {

                    switch (reader.getName().getLocalPart()) {

                        case "article":
                            article = new Article();
                            break;

                        case "id":
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setId(Integer.parseInt(reader.getText()));
                                }
                            }
                            break;

                        case "title":
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setTitle(reader.getText());
                                }
                            }
                            break;

                        case "content":
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setContent(reader.getText());
                                }
                            }
                            break;

                        case "time":
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setTime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(reader.getText()));
                                }
                            }
                            break;

                        case "category":
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setCategory(Category.valueOf(reader.getText()));
                                }
                            }
                            break;

                        case "author":
                            if (authors == null) authors = new ArrayList<>();
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                authors.add(reader.getText());
                            }
                            break;

                        case "text":
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                comment = new Comment(reader.getText());
                            }
                            break;

                        case "answer":
                            if (answers == null) answers = new ArrayList<>();
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                answers.add(reader.getText());
                            }
                            break;
                    }

                }

                if (eventType == XMLEvent.END_ELEMENT) {
                    if (reader.getName().getLocalPart().equals("article")) {
                        if (article != null) {
                            article.setAuthors(authors);
                            if (comment != null) {
                                comment.setAnswers(answers);
                                article.setComment(comment);
                            }
                            articles.add(article);
                            authors = null;
                            comment = null;
                            answers = null;
                        }
                    }
                }

            }
        } catch (XMLStreamException | FileNotFoundException | ParseException e) {
            System.out.println(e.getMessage());
        }
        return articles;
    }

    @Override
    public void write(String path, List<Article> articles) {
        XMLOutputFactory output = XMLOutputFactory.newInstance();

        try {
            XMLStreamWriter writer = output.createXMLStreamWriter(new FileOutputStream(path));
            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeCharacters("\n");


            // <news>
            writer.writeStartElement("news");
            writer.writeCharacters("\n");

            for (Article article : articles) {
                // <article>
                writer.writeCharacters("\t");
                writer.writeStartElement("article");
                writer.writeCharacters("\n\t\t");

                writer.writeStartElement("id");
                writer.writeCharacters(String.valueOf(article.getId()));
                writer.writeEndElement();
                writer.writeCharacters("\n\t\t");

                writer.writeStartElement("title");
                writer.writeCharacters(article.getTitle());
                writer.writeEndElement();
                writer.writeCharacters("\n\t\t");

                writer.writeStartElement("content");
                writer.writeCharacters(article.getContent());
                writer.writeEndElement();
                writer.writeCharacters("\n\t\t");

                writer.writeStartElement("time");
                writer.writeCharacters(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime()));
                writer.writeEndElement();
                writer.writeCharacters("\n\t\t");

                writer.writeStartElement("category");
                writer.writeCharacters(String.valueOf(article.getCategory()));
                writer.writeEndElement();
                writer.writeCharacters("\n\t\t");

                List<String> authors = article.getAuthors();
                writer.writeStartElement("authors");
                writer.writeCharacters("\n\t\t\t");
                for (int i = 0; i < authors.size(); i++) {
                    writer.writeStartElement("author");
                    writer.writeCharacters(authors.get(i));
                    writer.writeEndElement();
                    if (i != authors.size() - 1) writer.writeCharacters("\n\t\t\t");
                }
                writer.writeCharacters("\n\t\t");
                writer.writeEndElement();
                writer.writeCharacters("\n\t");

                Comment comment = article.getComment();
                if (comment != null) {
                    writer.writeCharacters("\t");
                    writer.writeStartElement("comment");
                    writer.writeCharacters("\n\t\t\t");

                    writer.writeStartElement("text");
                    writer.writeCharacters(article.getComment().getText());
                    writer.writeEndElement();
                    writer.writeCharacters("\n\t\t");

                    List<String> answers = article.getComment().getAnswers();
                    if (answers != null) {
                        writer.writeCharacters("\t");
                        writer.writeStartElement("answers");
                        writer.writeCharacters("\n\t\t\t\t");
                        for (int i = 0; i < answers.size(); i++) {
                            writer.writeStartElement("answer");
                            writer.writeCharacters(answers.get(i));
                            writer.writeEndElement();
                            if (i != answers.size() - 1) writer.writeCharacters("\n\t\t\t\t");
                        }
                        writer.writeCharacters("\n\t\t\t");
                        writer.writeEndElement();
                        writer.writeCharacters("\n\t\t");
                    }
                    writer.writeEndElement();
                    writer.writeCharacters("\n\t");
                }

                writer.writeEndElement();
                // </article>
                writer.writeCharacters("\n");
            }
            writer.writeEndDocument();
            // </news>

            writer.flush();
            writer.close();
        } catch (XMLStreamException | FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
