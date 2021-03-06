package ru.job4j.grabber;

import ru.job4j.html.SqlRuParse;
import ru.job4j.model.Post;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class PsqlStore implements Store, AutoCloseable {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader("./src/main/resources/grabber.properties"));
        } catch (IOException e) {
            e.getStackTrace();
            return;
        }
        SqlRuParse sqlRuParse = new SqlRuParse();
        Post post;
        String link = "https://www.sql.ru/forum/1334848/ishhem-android-developer";
        post = sqlRuParse.detail(link);
        try (PsqlStore store = new PsqlStore(properties)) {
            store.save(post);
            Post postFromDB = store.findById(String.valueOf(post.getId()));
            System.out.println(postFromDB.getLink());
        } catch (Exception e) {
            e.getStackTrace();
        }
        System.out.println();
        try (PsqlStore store = new PsqlStore(properties)) {
            store.save(post);
        } catch (Exception e) {
            e.getStackTrace();
        }
        System.out.println();
        try (PsqlStore store = new PsqlStore(properties)) {
            List<Post> posts = store.getAll();
            for (Post value : posts) {
                System.out.println(value.getLink());
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private Connection cnn;

    public PsqlStore(Properties cfg) throws SQLException {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        cnn = DriverManager.getConnection(
                cfg.getProperty("jdbc.url"),
                cfg.getProperty("jdbc.username"),
                cfg.getProperty("jdbc.password")
        );
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement ps = cnn.prepareStatement(
                "insert into post(title, description, link, timestamp) "
                        + "values (?, ?, ?, ?) on conflict (link) do nothing;",
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, post.getTitle());
            ps.setString(2, post.getDescription());
            ps.setString(3, post.getLink());
            ps.setTimestamp(4, post.getDate());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    post.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() throws SQLException {
        List<Post> posts = new ArrayList<>();
        try (PreparedStatement statement = cnn.prepareStatement("select * from post")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    posts.add(new Post(
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getString("link"),
                            resultSet.getTimestamp("timestamp")
                    ));
                }
            }
        }
        return posts;
    }

    @Override
    public Post findById(String id) throws SQLException {
        String sql = "select * from post where id = ?";
        try (PreparedStatement statement = cnn.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Post(
                            resultSet.getString("title"),
                            resultSet.getString("description"),
                            resultSet.getString("link"),
                            resultSet.getTimestamp("timestamp")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }
}