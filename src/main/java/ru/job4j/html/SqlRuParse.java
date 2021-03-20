package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.model.Post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    public static void main(String[] args) {
        System.out.println(new SqlRuParse().detail("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t"));
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

    @Override
    public List<Post> list(String link) throws IOException {
        List<Post> posts = new ArrayList<>();
        Document doc = Jsoup.connect(link).get();
        Elements row = doc.select(".postslisttopic");
        for (int i = 0; i < row.size(); i++) {
            Element href = row.get(i).child(0);
            posts.add(detail(href.attr("href")));
        }
        return posts;
    }

    public Post detail(String url) {
        Post post = new Post();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements comments = doc.select(".msgTable");
            String description = comments.first().select(".msgBody").get(1).html();
            String name = comments.first().select(".messageHeader").text();
            String date = comments.last().select(".msgFooter").text();
            date = date.substring(0, date.indexOf('[') - 1);
            return new Post(name, description, url, ParseDate.parse(date));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }
}
