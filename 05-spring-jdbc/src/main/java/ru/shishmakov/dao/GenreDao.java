package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Genre;

import java.util.List;

@Repository
public class GenreDao implements Dao<Genre> {
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
    public List<Genre> getAll() {
        return null;
    }
}
