package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Book;
import ru.shishmakov.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Optional<Book> getById(long id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void update(long id, Book data) {
        bookRepository.findById(id).ifPresent(b -> {
            b.setTitle(data.getTitle());
            b.setIsbn(data.getIsbn());
            bookRepository.save(b);
        });
    }
}
