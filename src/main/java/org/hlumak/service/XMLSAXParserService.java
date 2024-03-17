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

public class XMLSAXParserService implements ParserService<MapArticleObjectHandlerSax> {
    @Override
    public ArrayList<Article> parse(MapArticleObjectHandlerSax handler) {
        return (ArrayList<Article>) handler.getResult();
    }

    @Override
    public MapArticleObjectHandlerSax readFromFile(String filePath) {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        MapArticleObjectHandlerSax handler = new MapArticleObjectHandlerSax();

        try (InputStream is = Files.newInputStream(Paths.get(filePath))) {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            System.out.println(e.getMessage());
        }
        return handler;
    }
}
