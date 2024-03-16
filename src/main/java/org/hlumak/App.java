package org.hlumak;

import org.hlumak.entity.Article;
import org.hlumak.service.CSVParserService;
import java.util.ArrayList;

public class App 
{
    public static void main(String[] args) {
        CSVParserService csvParserService = new CSVParserService();
        String parseString =  csvParserService.readFromFile("C:\\Users\\XooJl\\Desktop\\Data_About_Articles.csv");
        ArrayList<Article> articles = csvParserService.parse(parseString);
        articles.forEach(article -> System.out.println(article.toString()));
    }

}
