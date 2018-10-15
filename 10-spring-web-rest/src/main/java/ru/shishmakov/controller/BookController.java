package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shishmakov.domain.Book;
import ru.shishmakov.dto.BookDto;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {
    private final LibraryService libraryService;

    @GetMapping
    public ResponseEntity<?> main() {
        return ResponseEntity
                .status(FOUND)
                .header(LOCATION, "/books")
                .body("RESTfull API for Book Library");
    }

    /**
     * List all books
     */
    @GetMapping("/books")
    public ResponseEntity<List<BookDto>> getAllBooks() {
        List<BookDto> books = libraryService.getAllBooks().stream()
                .map(BookDto::from)
                .collect(toList());
        return ResponseEntity.ok(books);
    }

    /**
     * Get one book by id
     */
    @GetMapping("/book/{id}")
    public ResponseEntity<BookDto> getBook(@PathVariable("id") long bookId) {
        Book book = libraryService.getBookById(bookId);
        return ResponseEntity.ok(BookDto.from(book));
    }

    /**
     * Insert new book
     */
    @PostMapping("/book")
    public ResponseEntity<BookDto> insertBook(@RequestBody BookDto data) {
        Book book = libraryService.createBook(data);
        return new ResponseEntity<>(BookDto.from(book), CREATED);
    }

    /**
     * Update existing book
     */
    @PutMapping("/book/{id}")
    public ResponseEntity<BookDto> editBook(@RequestBody BookDto data, @PathVariable("id") long id) {
        data.setId(id);
        Book book = libraryService.updateBook(data);
        return ResponseEntity.ok(BookDto.from(book));
    }

    /**
     * Delete existing book
     */
    @DeleteMapping(value = "/book/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") Long id) {
        libraryService.deleteBookById(id);
        return ResponseEntity.ok().build();
    }
}
