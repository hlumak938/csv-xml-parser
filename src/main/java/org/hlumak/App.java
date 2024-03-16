package org.hlumak;

import org.hlumak.service.CSVParserService;
import java.text.ParseException;

public class App 
{
    public static void main(String[] args) throws ParseException {
        new CSVParserService().parse("C:\\Users\\XoXoJl\\Desktop\\Data_About_Articles.csv");
    }

}
