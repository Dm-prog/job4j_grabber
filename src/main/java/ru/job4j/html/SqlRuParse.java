package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class SqlRuParse {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.US);

    public static void main(String[] args) throws Exception {
        parse();
    }

    public static void parse() throws IOException, ParseException {
        // Получаем HTML страницу
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
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
            Element data = td.parent().child(5);
            System.out.println(data.text());
            String dateInString = data.text();
            dateFormat.applyPattern(dateInString);
            Date date = dateFormat.parse(dateInString);
            System.out.println(dateFormat.format(date));
        }
    }
}
