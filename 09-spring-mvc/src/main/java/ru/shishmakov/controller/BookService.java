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

    public Book getById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("book:" + bookId + " not found"));
    }

    @Transactional
    public void update(Book data) {
        Book book = bookRepository.findById(data.getId())
                .orElseThrow(() -> new EntityNotFoundException("book:" + data.getId() + " not found"));
        book.setTitle(data.getTitle());
        book.setIsbn(data.getIsbn());
        bookRepository.save(book);
    }

    @Transactional
    public void create(Book data) {
        bookRepository.save(data);
    }
}
