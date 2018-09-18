package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Book;
import ru.shishmakov.repository.BookRepository;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }
}
