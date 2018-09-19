package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Book;
import ru.shishmakov.repository.BookRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book getById(long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("book:" + bookId + " not found"));
    }

    @Transactional
    public void update(long bookId, Book data) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("book:" + bookId + " not found"));
        book.setTitle(data.getTitle());
        book.setIsbn(data.getIsbn());
        bookRepository.save(book);
    }
}
