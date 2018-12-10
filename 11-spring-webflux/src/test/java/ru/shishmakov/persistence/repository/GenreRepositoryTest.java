package ru.shishmakov.persistence.repository;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.shishmakov.persistence.entity.Genre;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class GenreRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private GenreRepository genreRepository;
    private List<Genre> dbGenres;

    @Before
    public void setUp() {
        this.dbGenres = List.of(
                Genre.builder().name("genre 1").build(),
                Genre.builder().name("genre 2").build(),
                Genre.builder().name("genre 3").build());
        genreRepository.deleteAll()
                .thenMany(Flux.fromIterable(dbGenres)
                        .collectList()
                        .flatMapMany(genreRepository::saveAll))
                .then()
                .block();
    }

    @Test
    public void getAllShouldGetAllGenres() {
        Stream<Genre> genres = genreRepository.findAll().toStream();

        assertThat(genres)
                .isNotNull()
                .hasSize(dbGenres.size())
                .allMatch(Objects::nonNull, "all elements are not null")
                .allMatch(genre -> Objects.nonNull(genre.getId()), "all ids are initialized");
    }

    @Test
    public void findByIdShouldNotGetAuthorIfNotAvailable() {
        Mono<Genre> genre = genreRepository.findById(new ObjectId());

        StepVerifier
                .create(genre)
                .expectNextCount(0)
                .verifyComplete();
    }


    @Test
    public void findAllByIdShouldGetGenresIfTheyAvailable() {
        Set<ObjectId> ids = dbGenres.stream().map(Genre::getId).collect(toSet());
        Flux<Genre> genres = genreRepository.findAllById(ids);

        StepVerifier
                .create(genres)
                .expectNextCount(dbGenres.size())
                .verifyComplete();
    }

    @Test
    public void findAllByIdShouldNotGetGenresIfTheyNotAvailable() {
        Set<ObjectId> ids = Set.of(new ObjectId(), new ObjectId());
        Flux<Genre> genres = genreRepository.findAllById(ids);

        StepVerifier
                .create(genres)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deleteByIdShouldDeleteGenre() {
        ObjectId genresId = dbGenres.get(0).getId();
        Mono<Genre> genre = genreRepository.findById(genresId);
        StepVerifier
                .create(genre)
                .expectNextCount(1)
                .verifyComplete();

        Mono<Genre> deletedGenre = genreRepository.deleteById(genresId)
                .then(genreRepository.findById(genresId));
        StepVerifier
                .create(deletedGenre)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deleteAllShouldDeleteAllGenres() {
        Flux<Genre> genresBefore = genreRepository.findAll();
        StepVerifier
                .create(genresBefore)
                .expectNextCount(dbGenres.size())
                .verifyComplete();

        Flux<Genre> genresAfter = genreRepository
                .deleteAll()
                .thenMany(genreRepository.findAll());
        StepVerifier
                .create(genresAfter)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void createAuthorShouldSucceeded() {
        Mono<Genre> savedGenre = genreRepository.save(Genre.builder().name("new genre").build());

        StepVerifier
                .create(savedGenre)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void createGenreShouldThrowExceptionIfFullnameNull() {
        Mono<Genre> savedGenre = genreRepository.save(Genre.builder().name(null).build());

        StepVerifier
                .create(savedGenre)
                .expectNextCount(0)
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(ConstraintViolationException.class)
                .hasOperatorErrorWithMessage("name: must not be empty");
    }
}
