package org.hlumak;

import org.hlumak.bom.Article;
import org.hlumak.connector.CSVConnector;
import org.hlumak.connector.DOMConnector;
import org.hlumak.connector.SAXConnector;
import org.hlumak.controller.CSVController;
import org.hlumak.controller.DOMController;
import org.hlumak.controller.SAXController;
import org.hlumak.convertor.CSVConvertor;
import org.hlumak.convertor.DOMConvertor;
import org.hlumak.service.CSVService;
import org.hlumak.service.DOMService;
import org.hlumak.service.SAXService;

import java.text.ParseException;
import java.util.List;

public class App {
    public static void main(String[] args) throws ParseException {
        CSVController csvController = new CSVController(new CSVService(new CSVConnector(), new CSVConvertor()));
        List<Article> articlesCSV = csvController.getAll(args[0]);
        csvController.createFile(args[1], articlesCSV);

        DOMController domController = new DOMController(new DOMService(new DOMConnector(), new DOMConvertor()));
        List<Article> articlesDOM = domController.getAll(args[2]);
        domController.createFile(args[3], articlesDOM);

        SAXController saxController = new SAXController(new SAXService(new SAXConnector()));
        List<Article> articles = saxController.getAll(args[2]);
        saxController.createFile(args[4], articles);
    }

}
