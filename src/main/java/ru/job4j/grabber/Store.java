package ru.job4j.grabber;

import ru.job4j.model.Post;

import java.sql.SQLException;
import java.util.List;

public interface Store {
    void save(Post post);

    List<Post> getAll() throws SQLException;

    Post findById(String id) throws SQLException;
}
