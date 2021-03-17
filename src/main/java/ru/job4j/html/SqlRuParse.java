package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.model.Post;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SqlRuParse {

    public static void main(String[] args)  {
        System.out.println();
    }

    public static void parsePage(int page) throws IOException {
        String url = String.format("https://www.sql.ru/forum/job-offers/%d", page);
        // Получаем HTML страницу
        Document doc = Jsoup.connect(url).get();
        // Находим данные по критерию поиска ".postslisttopic"
        Elements row = doc.select(".postslisttopic");
        // Разбиваем по строкам
        for (Element td : row) {
            //Получаем сспервый элемент строки
            Element href = td.child(0);
            // Поучаем ссылку на строку
            System.out.println(href.attr("href"));
            //Получаем текст строки
            System.out.println(href.text());
            //Получаем 6-ой элемент от...
            Element date = td.parent().child(5);
            System.out.println(date.text());

            System.out.println(ParseDate.parse(date.text()));
        }
    }

    public static List<String> resources() throws IOException {
        List<String> list = new ArrayList<>();
        for (int page = 1; page <= 5; page++) {
            parsePage(page);
        }
        return list;
    }

    public Post detail(String link) {
        return new Post();
    }
}
