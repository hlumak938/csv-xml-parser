package org.hlumak;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.hlumak.service.CSVParserService;
import org.hlumak.entity.Article;
import org.hlumak.entity.Category;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class AppTest {

    private final CSVParserService csvParser = new CSVParserService();

    @Test
    public void testCsvParserMethod() throws ParseException {
        ArrayList<Article> expected = new ArrayList<>();
        expected.add(new Article(
                1,
                "New Study Shows Benefits of Exercise",
                "A recent study published in the Journal of Health Sciences reveals the numerous benefits of regular exercise, including improved cardiovascular health, increased energy levels, and reduced stress.",
                new SimpleDateFormat("dd.MM.yyyy HH:mm").parse("24.01.2023  14:03:00"),
                Category.HEALTH,
                new ArrayList<>(Arrays.asList("John Doe", " Nicolas Spark")))
        );
        ArrayList<Article> result = csvParser.parse("C:\\Users\\XoXoJl\\Desktop\\Data_About_Articles.csv");
        Assertions.assertEquals(expected.get(0).toString(), result.get(0).toString());
    }

}
