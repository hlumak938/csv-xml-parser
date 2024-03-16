package org.hlumak;

import org.hlumak.entity.Article;
import org.hlumak.entity.Category;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class App 
{
    public static void main(String[] args) throws ParseException {
        csvParse();
    }
    public static void csvParse() throws ParseException {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\XoXoJl\\Desktop\\Data_About_Articles.csv"))) {
            String line;

            while((line = br.readLine()) != null) {
                result.append(line);
                result.append('\n');
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String[] arr = result.toString().split("\n");
        ArrayList<Article> articleList = new ArrayList<>();
        for (int i = 1; i < arr.length; i++) {
            String[] values = arr[i].split(";");
            articleList.add(new Article((
                    Integer.parseInt(values[0])),
                    values[1],
                    values[2],
                    new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(values[3]),
                    Category.valueOf(values[4].toUpperCase()),
                    new ArrayList<>(Arrays.asList(values[5].split(",")))
            ));
        }

        for (Article article : articleList) {
            System.out.println(article.toString());
        }
    }
}
