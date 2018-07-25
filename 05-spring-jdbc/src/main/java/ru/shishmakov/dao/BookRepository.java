package ru.shishmakov.dao;

import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

import java.util.List;

@Repository
public class BookRepository implements IRepository<Book> {
    @Override
    public void save(Book genre) {

    }

    @Override
    public void update(Book genre) {

    }

    @Override
    public void delete(Book genre) {

    }

    @Override
    public Book getById(int id) {
        return null;
    }

    @Override
    public List<Book> getAll() {
        return null;
    }
}
