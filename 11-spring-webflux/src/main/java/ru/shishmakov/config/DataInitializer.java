package ru.shishmakov.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import ru.shishmakov.persistence.entity.Author;
import ru.shishmakov.persistence.entity.Book;
import ru.shishmakov.persistence.entity.Comment;
import ru.shishmakov.persistence.entity.Genre;
import ru.shishmakov.persistence.repository.AuthorRepository;
import ru.shishmakov.persistence.repository.BookRepository;
import ru.shishmakov.persistence.repository.GenreRepository;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

/**
 * Should define default set of data in storage
 */
@Slf4j
@RequiredArgsConstructor
@Component
class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("clean old data...");
        List<Author> authors = Arrays.asList(
                Author.builder().fullname("author 1").build(),
                Author.builder().fullname("author 2").build(),
                Author.builder().fullname("author 3").build(),
                Author.builder().fullname("author 4").build());
        List<Genre> genres = Arrays.asList(
                Genre.builder().name("genre 1").build(),
                Genre.builder().name("genre 2").build(),
                Genre.builder().name("genre 3").build());

        authorRepository.deleteAll()
                .thenMany(Flux.fromIterable(authors)
                        .collectList()
                        .flatMapMany(authorRepository::saveAll))
                .thenMany(authorRepository.findAll())
                .collectList()
                .doOnSuccess(data -> log.info("init authors: {}", data))
                .block();
        genreRepository.deleteAll()
                .thenMany(Flux.fromIterable(genres)
                        .collectList()
                        .flatMapMany(genreRepository::saveAll))
                .thenMany(genreRepository.findAll())
                .collectList()
                .doOnSuccess(data -> log.info("init genres: {}", data))
                .block();

        bookRepository.deleteAll()
                .thenMany(Flux.just(
                        Book.builder().title("book 1").isbn("0-395-08254-1").comments(buildComments(3)).build()
                                .addAuthors(filter(authors, Author::getFullname, "author 1", "author 2", "author 3", "author 4"))
                                .addGenres(filter(genres, Genre::getName, "genre 1", "genre 2")),
                        Book.builder().title("book 2").isbn("0-395-08254-2").comments(buildComments(2)).build()
                                .addAuthors(filter(authors, Author::getFullname, "author 1", "author 3"))
                                .addGenres(filter(genres, Genre::getName, "genre 2")),
                        Book.builder().title("book 3").isbn("0-395-08254-3").build(),
                        Book.builder().title("book 4").isbn("0-395-08254-4").comments(buildComments(1)).build()
                                .addAuthors(filter(authors, Author::getFullname, "author 4"))
                                .addGenres(filter(genres, Genre::getName, "genre 1")))
                        .collectList()
                        .flatMapMany(bookRepository::saveAll))
                .thenMany(bookRepository.findAll())
                .collectList()
                .doOnSuccess((List<Book> data) -> log.info("init books: {}", data.stream()
                        .map(Book::toString)
                        .collect(joining("\n", "\n", ""))))
                .thenMany(authorRepository.saveAll(authors))
                .thenMany(genreRepository.saveAll(genres))
                .then().block();
    }

    private Set<Comment> buildComments(int count) {
        return LongStream.range(0, count)
                .map(id -> id + 1)
                .mapToObj(id -> Comment.builder().id(new ObjectId())
                        .createDate(Instant.now())
                        .text("comment " + id)
                        .build())
                .collect(toSet());
    }

    private <T> Set<T> filter(List<T> src, Function<T, String> function, String... items) {
        Set<String> set = Set.of(items);
        return src.stream()
                .filter(a -> set.contains(function.apply(a)))
                .collect(toSet());
    }
}
