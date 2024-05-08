package org.hlumak.converter.strategy;

import com.sun.xml.txw2.output.IndentingXMLStreamWriter;
import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import javax.xml.stream.events.XMLEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ArticleSAXConverterStrategy implements ConverterStrategy {

    private final String ARTICLE = "article";
    private final String ID = "id";
    private final String TITLE = "title";
    private final String CONTENT = "content";
    private final String TIME = "time";
    private final String CATEGORY = "category";
    private final String AUTHOR = "author";
    private final String COMMENT_TEXT = "text";
    private final String COMMENT_ANSWER = "answer";

    @Override
    public byte[] toDTO(List<Article> articles) {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        try {
            XMLStreamWriter writer = new IndentingXMLStreamWriter(output.createXMLStreamWriter(result));
            writer.writeStartDocument("UTF-8", "1.0");

            // <news>
            writer.writeStartElement("news");

            for (Article article : articles) {
                // <article>
                writer.writeStartElement(ARTICLE);

                writer.writeStartElement(ID);
                writer.writeCharacters(String.valueOf(article.getId()));
                writer.writeEndElement();

                writer.writeStartElement(TITLE);
                writer.writeCharacters(article.getTitle());
                writer.writeEndElement();

                writer.writeStartElement(CONTENT);
                writer.writeCharacters(article.getContent());
                writer.writeEndElement();

                writer.writeStartElement(TIME);
                writer.writeCharacters(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime()));
                writer.writeEndElement();

                writer.writeStartElement(CATEGORY);
                writer.writeCharacters(String.valueOf(article.getCategory()));
                writer.writeEndElement();

                List<String> authors = article.getAuthors();
                writer.writeStartElement("authors");
                for (String author : authors) {
                    writer.writeStartElement(AUTHOR);
                    writer.writeCharacters(author);
                    writer.writeEndElement();
                }
                writer.writeEndElement();

                Comment comment = article.getComment();
                if (comment != null) {
                    writer.writeStartElement("comment");

                    writer.writeStartElement(COMMENT_TEXT);
                    writer.writeCharacters(article.getComment().getText());
                    writer.writeEndElement();

                    List<String> answers = article.getComment().getAnswers();
                    if (answers != null) {
                        writer.writeStartElement("answers");
                        for (String answer : answers) {
                            writer.writeStartElement(COMMENT_ANSWER);
                            writer.writeCharacters(answer);
                            writer.writeEndElement();
                        }
                        writer.writeEndElement();
                    }
                    writer.writeEndElement();
                }

                writer.writeEndElement();
                // </article>
            }
            writer.writeEndDocument();
            // </news>

            writer.flush();
            writer.close();
        } catch (XMLStreamException e) {
            throw new RuntimeException("Couldn't parse XML file! Path: ", e);
        }

        return result.toByteArray();
    }

    @Override
    public List<Article> fromDTO(byte[] bytes) {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        xmlInputFactory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        List<Article> result = new ArrayList<>();

        try {
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(bytes));

            Article article = null;
            List<String> authors = new ArrayList<>();
            Comment comment = null;
            List<String> answers = null;

            while (reader.hasNext()) {

                int eventType = reader.next();

                if (eventType == XMLEvent.START_ELEMENT) {

                    switch (reader.getName().getLocalPart()) {

                        case ID:
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                article = new Article(Integer.parseInt(reader.getText()));
                            }
                            break;

                        case TITLE:
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setTitle(reader.getText());
                                }
                            }
                            break;

                        case CONTENT:
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setContent(reader.getText());
                                }
                            }
                            break;

                        case TIME:
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setTime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(reader.getText()));
                                }
                            }
                            break;

                        case CATEGORY:
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                if (article != null) {
                                    article.setCategory(Category.valueOf(reader.getText()));
                                }
                            }
                            break;

                        case AUTHOR:
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                authors.add(reader.getText());
                            }
                            break;

                        case COMMENT_TEXT:
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                comment = new Comment(reader.getText());
                            }
                            break;

                        case COMMENT_ANSWER:
                            if (answers == null) answers = new ArrayList<>();
                            eventType = reader.next();
                            if (eventType == XMLEvent.CHARACTERS) {
                                answers.add(reader.getText());
                            }
                            break;
                    }

                }

                if (eventType == XMLEvent.END_ELEMENT) {
                    if (reader.getName().getLocalPart().equals(ARTICLE)) {
                        if (article != null) {
                            article.setAuthors(authors);
                            if (comment != null) {
                                comment.setAnswers(answers);
                                article.setComment(comment);
                            }
                            result.add(article);
                            authors = new ArrayList<>();
                            comment = null;
                            answers = null;
                        }
                    }
                }

            }
        } catch (XMLStreamException e) {
            throw new RuntimeException("Couldn't parse XML file! Path: ", e);
        } catch (ParseException e) {
            throw new RuntimeException("Couldn't parse date! Path: ", e);
        }

        return result;
    }

    @Override
    public String getExtension() {
        return ".xml";
    }
}
