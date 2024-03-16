package org.hlumak.service;

import org.hlumak.entity.Article;
import org.hlumak.entity.Category;
import org.hlumak.entity.Comment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class CSVParserService {
    public ArrayList<Article> parse(String filePath) throws ParseException {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String[] strings = result.toString().split("\n");
        ArrayList<Article> articleList = new ArrayList<>();
        for (int i = 1; i < strings.length; i++) {
            String[] values = strings[i].split(";");
            articleList.add(new Article(
                    Integer.parseInt(values[0]),
                    values[1],
                    values[2],
                    new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(values[3]),
                    Category.valueOf(values[4].toUpperCase()),
                    new ArrayList<>(Arrays.asList(values[5].split(","))),
                    new Comment(values.length == 7 ? values[6] : null, values.length == 8 ? Arrays.asList(values[7].split(",")) : null)
            ));
        }

        return articleList;
    }
}
