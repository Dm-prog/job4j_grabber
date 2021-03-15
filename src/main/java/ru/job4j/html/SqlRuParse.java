package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlRuParse {

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

            ParseDate parseDate = new ParseDate();
            System.out.println(parseDate.parse(date.text()));
        }
    }
}
