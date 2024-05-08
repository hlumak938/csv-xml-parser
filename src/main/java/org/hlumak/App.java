package org.hlumak;

import lombok.extern.slf4j.Slf4j;
import org.hlumak.bom.Article;
import org.hlumak.controller.ArticleController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
                result = switch (exception.getClass().getSimpleName()) {
                    case "FileNotFoundException" -> 1;
                    case "IOException" -> 2;
                    case "ParseException" -> 3;
                    case "JDOMException" -> 4;
                    case "XMLStreamException" -> 5;
                    default -> -1;
                };
            }
            return result;
        };
    }

    @Bean
    public CommandLineRunner commandLineRunner(ArticleController articleController) {
        return args -> {
            String filePath = "src/test/resources/files/Articles";

            articleController.setConvertor("CSV");

            List<Article> articles = articleController.getAll(filePath);
            articles.forEach(article -> log.info(article.toString()));
            articleController.createFile("src/test/resources/files/Articles_new", articles);

            articleController.setConvertor("DOM");

            articles = articleController.getAll(filePath);
            articles.forEach(article -> log.info(article.toString()));
            articleController.createFile("src/test/resources/files/Articles_new", articles);

            articleController.setConvertor("SAX");

            articles = articleController.getAll(filePath);
            articles.forEach(article -> log.info(article.toString()));
            articleController.createFile("src/test/resources/files/Articles_new_sax", articles);
        };
    }
}
