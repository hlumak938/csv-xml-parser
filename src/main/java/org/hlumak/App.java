package org.hlumak;

import lombok.extern.slf4j.Slf4j;
import org.hlumak.bom.Article;
import org.hlumak.controller.ArticleController;
import org.hlumak.convertor.ArticleDOMConvertorStrategy;
import org.hlumak.convertor.ArticleSAXConvertorStrategy;
import org.jdom2.JDOMException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@SpringBootApplication
@Slf4j
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public ExitCodeExceptionMapper exitCodeToExceptionMapper() {
        return exception -> {
            int result = 0;
            if (exception != null) {
                log.error("Exit with error{}", exception.getClass(), exception);
                switch (exception) {
                    case FileNotFoundException ignored -> result = 1;
                    case IOException ignored -> result = 2;
                    case ParseException ignored -> result = 3;
                    case JDOMException ignored -> result = 4;
                    case XMLStreamException ignored -> result = 5;
                    default -> result = -1;
                }
            }
            return result;
        };
    }

    @Bean
    public CommandLineRunner commandLineRunner(ArticleController articleController) {
        return args -> {
            String filePath = "team-dv/src/test/resources/files/Articles";

            List<Article> articles = articleController.getAll(filePath);
            articles.forEach(article -> log.info(article.toString()));
            articleController.createFile("team-dv/src/test/resources/files/Articles_new", articles);

            articleController.setConvertor(new ArticleDOMConvertorStrategy());

            articles = articleController.getAll(filePath);
            articles.forEach(article -> log.info(article.toString()));
            articleController.createFile("team-dv/src/test/resources/files/Articles_new", articles);

            articleController.setConvertor(new ArticleSAXConvertorStrategy());

            articles = articleController.getAll(filePath);
            articles.forEach(article -> log.info(article.toString()));
            articleController.createFile("team-dv/src/test/resources/files/Articles_new_sax", articles);
        };
    }
}
