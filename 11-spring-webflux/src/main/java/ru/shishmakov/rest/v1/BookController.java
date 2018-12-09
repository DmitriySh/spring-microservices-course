package ru.shishmakov.rest.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.shishmakov.service.LibraryService;
import ru.shishmakov.web.dto.BookDto;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BookController {
    private final LibraryService libraryService;

    @GetMapping
    public Mono<ResponseEntity<?>> main() {
        return Mono.just(ResponseEntity
                .status(FOUND)
                .header(LOCATION, "/books")
                .body("RESTful API for Book Library"));
    }

    /**
     * List all books
     */
    @GetMapping("/books")
    public Flux<BookDto> getAllBooks() {
        return libraryService.getAllBooks()
                .map(BookDto::from);
    }

    /**
     * Get one book by id
     */
    @GetMapping("/book/{id}")
    public Mono<ResponseEntity<BookDto>> getBook(@PathVariable("id") ObjectId bookId) {
        return libraryService.getBookById(bookId)
                .map(BookDto::from)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    /**
     * Insert new book
     */
    @PostMapping("/book")
    public Mono<ResponseEntity<BookDto>> insertBook(@RequestBody BookDto data) {
        return libraryService.createBook(data)
                .map(BookDto::from)
                .map(ResponseEntity.status(CREATED)::body);
    }

    /**
     * Update existing book
     */
//    @PutMapping("/book/{id}")
//    public Mono<ResponseEntity<BookDto>> editBook(@RequestBody BookDto data, @PathVariable("id") ObjectId id) {
//        data.setId(id);
//        return libraryService.updateBook(data)
//                .map(BookDto::from)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }

    /**
     * Delete existing book
     */
//    @DeleteMapping(value = "/book/{id}")
//    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") ObjectId id) {
//        return libraryService.deleteBook(id)
//                .thenReturn(ResponseEntity.ok().<Void>build())
//                .defaultIfEmpty(ResponseEntity.notFound().<Void>build());
//    }
}
