package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
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
        }
    }
}
