package ru.shishmakov.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import ru.shishmakov.domain.Book;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BookDao implements Dao<Book> {
    private final JdbcOperations jdbc;

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
//        jdbc.query("SELECT * FROM book join ");
        return null;
    }
}
