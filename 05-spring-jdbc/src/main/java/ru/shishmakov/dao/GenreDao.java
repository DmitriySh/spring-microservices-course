package ru.shishmakov.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class GenreDao implements Dao<Genre> {
    private final JdbcOperations jdbc;

    @Override
    public void save(Genre genre) {

    }

    @Override
    public void update(Genre genre) {

    }

    @Override
    public void delete(Genre genre) {

    }

    @Override
    public Genre getById(int id) {
        return null;
    }

    @Override
    public Collection<Genre> getAll() {
        return jdbc.query("select * from genre", rs -> {
            List<Genre> result = new ArrayList<>();
            while (rs.next()) result.add(new Genre(rs.getLong("id"), rs.getString("name")));
            return result;
        });
    }
}
