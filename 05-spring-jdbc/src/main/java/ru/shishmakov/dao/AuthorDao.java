package ru.shishmakov.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class AuthorDao implements Dao<Author> {
    private final JdbcOperations jdbc;

    @Override
    public void save(Author genre) {

    }

    @Override
    public void update(Author genre) {

    }

    @Override
    public void delete(Author genre) {

    }

    @Override
    public Author getById(int id) {
        return null;
    }

    @Override
    public Collection<Author> getAll() {
        return jdbc.query("select * from author", rs -> {
            List<Author> result = new ArrayList<>();
            while (rs.next()) result.add(new Author(rs.getLong("id"), rs.getString("fullname")));
            return result;
        });
    }
}
