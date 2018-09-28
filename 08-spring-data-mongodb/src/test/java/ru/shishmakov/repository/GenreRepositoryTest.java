package ru.shishmakov.repository;

import org.assertj.core.util.Sets;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.domain.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test MongoDb Data layer without Web
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
        genreRepository.deleteAll();
        this.dbGenres = requireNonNull(genreRepository.saveAll(Arrays.asList(
                Genre.builder().name("genre 1").build(),
                Genre.builder().name("genre 2").build(),
                Genre.builder().name("genre 3").build())));
    }

    @Test
    public void findAllShouldGetAllGenres() {
        List<Genre> authors = genreRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(dbGenres)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void findAllByIdShouldGetGenresIfTheyAvailable() {
        Set<ObjectId> ids = dbGenres.stream().map(Genre::getId).collect(toSet());
        List<Genre> genres = genreRepository.findAllById(ids);

        assertThat(genres)
                .isNotNull()
                .hasSameSizeAs(dbGenres)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void findAllByIdShouldNotGetGenresIfTheyNotAvailable() {
        Set<ObjectId> ids = Sets.newLinkedHashSet(new ObjectId(), new ObjectId());
        List<Genre> genres = genreRepository.findAllById(ids);

        assertThat(genres)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void deleteAllShouldDeleteAllAuthors() {
        List<Genre> genresBefore = genreRepository.findAll();
        assertThat(genresBefore)
                .isNotNull()
                .isNotEmpty();

        genreRepository.deleteAll();

        List<Genre> genresAfter = genreRepository.findAll();
        assertThat(genresAfter)
                .isNotNull()
                .isEmpty();
    }
}
