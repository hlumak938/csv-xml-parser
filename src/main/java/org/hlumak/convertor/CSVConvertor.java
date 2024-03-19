package org.hlumak.convertor;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;
import org.hlumak.dto.CSVDTO;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVConvertor implements Convertor<CSVDTO> {
    @Override
    public CSVDTO toDTO(List<Article> articles) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Field field : Article.class.getDeclaredFields()) {
            stringBuilder.append(field.getName()).append(";");
        }
        stringBuilder.append("answers");
        stringBuilder.append("\n");

        for (Article article : articles) {
            stringBuilder.append(article.getId()).append(";")
                    .append(article.getTitle()).append(";");

            String content = article.getContent().replace("\"", "\"\"");
            if (content.contains(";")) {
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
            if ((comment = article.getComment()) != null) {
                stringBuilder.append(comment.getText()).append(";");
                List<String> answers;
                if ((answers = comment.getAnswers()) != null) {
                    for (String answer : answers) {
                        stringBuilder.append(answer).append(",");
                    }
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }
            } else stringBuilder.append(";");
            stringBuilder.append("\n");
        }
        return new CSVDTO(stringBuilder.toString());
    }

    @Override
    public List<Article> fromDTO(CSVDTO dto) throws ParseException {
        String[] strings = dto.getParseString().split("\n");
        ArrayList<Article> articleList = new ArrayList<>();
        for (int i = 1; i < strings.length; i++) {
            String[] values = strings[i].split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

            int id = Objects.equals(values[0], "") ? 0 : Integer.parseInt(values[0]);
            String title = values[1];
            String content = values[2].replaceAll("^\"|\"$", "").replace("\"\"", "\"");

            Date articleTime = new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(values[3]);

            Category category = Category.valueOf(values[4].toUpperCase());
            List<String> authors = Arrays.asList(values[5].split(","));

            Comment comment = null;
            if (values.length == 7) {
                comment = new Comment(values[6]);
            } else if (values.length == 8) {
                comment = new Comment(values[6], Arrays.asList(values[7].split(",")));
            }

            articleList.add(new Article(id, title, content, articleTime, category, authors, comment));
        }

        return articleList;
    }
}
