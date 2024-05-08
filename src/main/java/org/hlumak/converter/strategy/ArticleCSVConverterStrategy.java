package org.hlumak.converter.strategy;

import org.hlumak.bom.Article;
import org.hlumak.bom.Category;
import org.hlumak.bom.Comment;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ArticleCSVConverterStrategy implements ConverterStrategy {

    @Override
    public byte[] toDTO(List<Article> articles) {
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
        return stringBuilder.toString().getBytes();
    }

    @Override
    public List<Article> fromDTO(byte[] bytes) {
        try {
            String parseString = new String(bytes);
            String[] strings = parseString.split("\r\n");
            ArrayList<Article> result = new ArrayList<>();
            for (int i = 1; i < strings.length; i++) {
                String[] values = strings[i].split(";(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                Article article = new Article(Integer.parseInt(values[0]));
                article.setTitle(values[1]);
                article.setContent(values[2].replaceAll("^\"|\"$", "").replace("\"\"", "\""));
                article.setTime(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(values[3]));
                article.setCategory(Category.valueOf(values[4].toUpperCase()));
                article.setAuthors(List.of(values[5].split(",")));

                final int COMMENT_POSITION = 7;
                final int ANSWERS_POSITION = 8;
                if (values.length == COMMENT_POSITION) {
                    article.setComment(new Comment(values[6]));
                } else if (values.length == ANSWERS_POSITION) {
                    article.setComment(new Comment(values[6]));
                    article.getComment().setAnswers(List.of(values[7].split(",")));
                }

                result.add(article);
            }

            return result;
        } catch (ParseException e) {
            throw new RuntimeException("Couldn't parse bytes!", e);
        }
    }

    @Override
    public String getExtension() {
        return ".csv";
    }
}
