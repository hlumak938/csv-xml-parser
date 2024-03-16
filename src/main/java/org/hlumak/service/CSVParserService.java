package org.hlumak.service;

import org.hlumak.entity.Article;
import org.hlumak.entity.Category;
import org.hlumak.entity.Comment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVParserService implements ParserService<String> {
    @Override
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
                    Objects.equals(values[0], "") ? 0 : Integer.parseInt(values[0]),
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

    @Override
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

    @Override
    public void writeInFile(String filePath, ArrayList<Article> articles) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Field field : Article.class.getDeclaredFields()) {
                stringBuilder.append(field.getName()).append(";");
            }
            stringBuilder.append("answers");
            stringBuilder.append("\n");
            for(Article article : articles) {
                stringBuilder.append(article.getId()).append(";")
                        .append(article.getTitle()).append(";");

                String content = article.getContent().replace("\"", "\"\"");
                if(content.contains(";")) {
                    stringBuilder.append("\"").append(content).append("\"").append(";");
                } else {
                    stringBuilder.append(article.getContent()).append(";");
                }

                stringBuilder.append(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(article.getTime())).append(";");

                String category = article.getCategory().toString();
                stringBuilder.append(category.charAt(0)).append(category.substring(1).toLowerCase()).append(";");

                for (String author : article.getAuthors()) {
                    stringBuilder.append(author).append(",");
                }
                stringBuilder.deleteCharAt(stringBuilder.length() - 1).append(";");

                Comment comment;
                if((comment = article.getComment()) != null) {
                    stringBuilder.append(comment.getText()).append(";");
                    List<String> answers;
                    if((answers = comment.getAnswers()) != null) {
                        for (String answer : answers) {
                            stringBuilder.append(answer).append(",");
                        }
                        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                    }
                } else stringBuilder.append(";");
                stringBuilder.append("\n");
            }
            bw.write(stringBuilder.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
