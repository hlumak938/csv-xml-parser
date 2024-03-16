package org.hlumak;

import org.hlumak.entity.Article;
import org.hlumak.service.CSVParserService;
import org.hlumak.service.XMLParserService;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class App 
{
    public static void main(String[] args) {
        CSVParserService csvParserService = new CSVParserService();
        String parseString =  csvParserService.readFromFile("files/Data_About_Articles.csv");
        ArrayList<Article> articlesCSV = csvParserService.parse(parseString);
        // articlesCSV.forEach(System.out::println);

        XMLParserService xmlParserService = new XMLParserService();
        xmlParserService.writeInFile("files/Data_About_Articles.xml", articlesCSV);
        List<Element> articleElements = xmlParserService.readFromFile("files/Data_About_Articles.xml");
        ArrayList<Article> articlesXML = xmlParserService.parse(articleElements);
        articlesXML.forEach(System.out::println);
    }

}
