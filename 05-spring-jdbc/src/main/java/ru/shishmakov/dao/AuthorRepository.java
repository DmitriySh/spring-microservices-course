package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Author;

import java.util.List;

@Repository
public class AuthorRepository implements GenericRepository<Author> {
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
    public List<Author> getAll() {
        return null;
    }
}
