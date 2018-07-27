package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import java.util.Collection;

@Repository
public class AuthorDao implements Dao<Author> {
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
        return null;
    }
}
