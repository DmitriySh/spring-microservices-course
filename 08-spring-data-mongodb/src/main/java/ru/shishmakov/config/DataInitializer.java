package ru.shishmakov.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.PriorityOrdered;
import org.springframework.shell.Shell;
import org.springframework.stereotype.Component;
import ru.shishmakov.domain.Author;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;
import ru.shishmakov.domain.Genre;
import ru.shishmakov.repository.AuthorRepository;
import ru.shishmakov.repository.BookRepository;
import ru.shishmakov.repository.GenreRepository;

import javax.annotation.Priority;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.LongStream;

import static java.util.stream.Collectors.toSet;

/**
 * Should be invoke before {@link Shell} bean and define default set of data in storage
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Priority(value = PriorityOrdered.HIGHEST_PRECEDENCE)
class DataInitializer implements CommandLineRunner {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;

    @Override
    public void run(String... args) {
        log.info("clean old data...");
        authorRepository.deleteAll();
        bookRepository.deleteAll();
        genreRepository.deleteAll();

        log.info("init default data...");

        List<Author> authors = Arrays.asList(
                Author.builder().fullname("author 1").build(),
                Author.builder().fullname("author 2").build(),
                Author.builder().fullname("author 3").build(),
                Author.builder().fullname("author 4").build());
        authorRepository.saveAll(authors);

        List<Genre> genres = Arrays.asList(
                Genre.builder().name("genre 1").build(),
                Genre.builder().name("genre 2").build(),
                Genre.builder().name("genre 3").build());
        genreRepository.saveAll(genres);

        List<Book> books = Arrays.asList(
                Book.builder().title("book 1").isbn("0-395-08254-1").comments(buildComments(3)).build()
                        .addAuthors(filter(authors, Author::getFullname, "author 1", "author 2", "author 3", "author 4"))
                        .addGenres(filter(genres, Genre::getName, "genre 1", "genre 2")),
                Book.builder().title("book 2").isbn("0-395-08254-2").comments(buildComments(2)).build()
                        .addAuthors(filter(authors, Author::getFullname, "author 1", "author 3"))
                        .addGenres(filter(genres, Genre::getName, "genre 2")),
                Book.builder().title("book 3").isbn("0-395-08254-3").build(),
                Book.builder().title("book 4").isbn("0-395-08254-4").comments(buildComments(1)).build()
                        .addAuthors(filter(authors, Author::getFullname, "author 4"))
                        .addGenres(filter(genres, Genre::getName, "genre 1")));
        bookRepository.saveAll(books);
        authorRepository.saveAll(authors);
        genreRepository.saveAll(genres);
        log.info("authors: {}", authorRepository.findAll());
        log.info("genres: {}", genreRepository.findAll());
        log.info("books: {}", bookRepository.findAll());
    }

    private Set<Comment> buildComments(int count) {
        return LongStream.range(0, count)
                .map(id -> id + 1)
                .mapToObj(id -> Comment.builder().id(new ObjectId()).createDate(Instant.now()).text("comment " + id).build())
                .collect(toSet());
    }

    private <T> Set<T> filter(List<T> src, Function<T, String> function, String... items) {
        Set<String> set = Set.of(items);
        return src.stream()
                .filter(a -> set.contains(function.apply(a)))
                .collect(toSet());
    }
}
