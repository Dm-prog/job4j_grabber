package ru.job4j.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Post {
    private int id;
    private String title;
    private String description;
    private String link;
    private Timestamp date;

    public Post() {
    }

    public Post(String title, String description, String link, Timestamp date) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
    }

    public Post(int id, String title, String description, String link, Timestamp date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.link = link;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{ title='" + title + '\'' + ", description='"
                + description + '\'' + ", link='" + link + '\''
                + ", date=" + date + '}';
    }
}