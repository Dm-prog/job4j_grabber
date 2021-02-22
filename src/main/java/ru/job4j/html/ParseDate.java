package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ParseDate {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.US);
    public void parse() throws IOException, ParseException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element href = td.child(0);
            System.out.println(href.attr("href"));
            System.out.println(href.text());

            Element data = td.parent().child(5);
            System.out.println(data.text());
            String dateInString = data.text();
            dateFormat.applyPattern(dateInString);
            Date date = dateFormat.parse(dateInString);
            System.out.println(dateFormat.format(date));
        }
    }
}
