package org.hlumak;

import org.hlumak.entity.Article;
import org.hlumak.service.CSVParserService;
import java.text.ParseException;
import java.util.ArrayList;

public class App 
{
    public static void main(String[] args) throws ParseException {
        new CSVParserService().parse("C:\\Users\\XoXoJl\\Desktop\\Data_About_Articles.csv");
        ArrayList<Article> articles = new CSVParserService().parse("C:\\Users\\XoXoJl\\Desktop\\Data_About_Articles.csv");
        articles.forEach(article -> System.out.println(article.toString()));
    }

}
