package org.hlumak.service;

import org.hlumak.entity.Article;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XMLSAXParserService implements ParserService<String> {
    @Override
    public ArrayList<Article> parse(String obj) {
        return null;
    }

    @Override
    public String readFromFile(String filePath) {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        StringBuilder stringBuilder = new StringBuilder();
        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {

            SAXParser saxParser = factory.newSAXParser();

            // parse XML and map to object, it works, but not recommend, try JAXB
            MapArticleObjectHandlerSax handler = new MapArticleObjectHandlerSax();

            saxParser.parse(is, handler);

            // print all
            List<Article> result = handler.getResult();
            result.forEach(article -> stringBuilder.append(article).append("\n"));

        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
        }

        return stringBuilder.toString();
    }

    @Override
    public void writeInFile(String filePath, ArrayList<Article> articles) {

    }
}
