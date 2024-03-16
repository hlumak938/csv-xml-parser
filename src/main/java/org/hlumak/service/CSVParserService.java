package org.hlumak.service;

import org.hlumak.entity.Article;
import org.hlumak.entity.Category;
import org.hlumak.entity.Comment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class CSVParserService {
    public ArrayList<Article> parse(String stringForParse)  {
        String[] strings = stringForParse.split("\n");
        ArrayList<Article> articleList = new ArrayList<>();
        for (int i = 1; i < strings.length; i++) {
            String[] values = strings[i].split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            Date articleTime = null;
            try {
                articleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(values[3]);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            articleList.add(new Article(
                    Integer.parseInt(values[0]),
                    values[1],
                    values[2].replaceAll("^\"|\"$", "").replace("\"\"", "\""),
                    articleTime,
                    Category.valueOf(values[4].toUpperCase()),
                    new ArrayList<>(Arrays.asList(values[5].split(","))),
                    values.length == 7 ? new Comment(values[6]) : values.length == 8 ? new Comment(values[6], Arrays.asList(values[7].split(","))) : null
            ));
        }

        return articleList;
    }

    public String readFromFile(String filePath) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while((line = br.readLine()) != null) {
                result.append(line).append("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result.toString();
    }
}
