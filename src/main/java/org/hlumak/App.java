package org.hlumak;

import org.hlumak.entity.Article;
import org.hlumak.service.CSVParserService;
import org.hlumak.service.MapArticleObjectHandlerSax;
import org.hlumak.service.XMLDOMParserService;
import org.hlumak.service.XMLSAXParserService;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

public class App 
{
    public static void main(String[] args) {
        System.out.println("CSV PARSER:");
        CSVParserService csvParserService = new CSVParserService();
        String parseString =  csvParserService.readFromFile("files/Data_About_Articles.csv");
        ArrayList<Article> articlesCSV = csvParserService.parse(parseString);
        articlesCSV.forEach(System.out::println);
        csvParserService.writeInFile("files/Data_About_Articles.csv", articlesCSV);

        System.out.println("\nXML PARSER(DOM):");
        XMLDOMParserService XMLDOMParserService = new XMLDOMParserService();
        XMLDOMParserService.writeInFile("files/Data_About_Articles.xml", articlesCSV);
        List<Element> articleElements = XMLDOMParserService.readFromFile("files/Data_About_Articles.xml");
        ArrayList<Article> articlesXML = XMLDOMParserService.parse(articleElements);
        articlesXML.forEach(System.out::println);

        System.out.println("\nXML PARSER(SAX):");
        XMLSAXParserService xmlSAXParserService = new XMLSAXParserService();
        MapArticleObjectHandlerSax mapArticleObjectHandlerSax = xmlSAXParserService.readFromFile("files/Data_About_Articles.xml");
        ArrayList<Article> articlesXMLSAX = xmlSAXParserService.parse(mapArticleObjectHandlerSax);
        articlesXMLSAX.forEach(System.out::println);
    }

}
