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
import ru.shishmakov.domain.Author;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test MongoDb Data layer without Web
 */
@RunWith(SpringRunner.class)
@DataMongoTest
public class AuthorRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();

    @Autowired
    private AuthorRepository authorRepository;
    private List<Author> dbAuthors;

    @Before
    public void setUp() {
        authorRepository.deleteAll();
        this.dbAuthors = requireNonNull(authorRepository.saveAll(Arrays.asList(
                Author.builder().fullname("author 1").build(),
                Author.builder().fullname("author 2").build(),
                Author.builder().fullname("author 3").build(),
                Author.builder().fullname("author 4").build())));
    }

    @Test
    public void findByIdShouldGetAuthor() {
        Optional<Author> author = authorRepository.findById(dbAuthors.get(0).getId());

        assertThat(author)
                .isNotNull()
                .isPresent()
                .get().isIn(dbAuthors);
    }

    @Test
    public void findByIdShouldNotGetAuthorIfNotAvailable() {
        Optional<Author> author = authorRepository.findById(new ObjectId());

        assertThat(author)
                .isNotNull()
                .isNotPresent();
    }

    @Test
    public void findAllShouldGetAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .isNotEmpty()
                .hasSameSizeAs(dbAuthors)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void findAllByIdShouldGetAuthorsIfTheyAvailable() {
        Set<ObjectId> ids = dbAuthors.stream().map(Author::getId).collect(toSet());
        List<Author> authors = authorRepository.findAllById(ids);

        assertThat(authors)
                .isNotNull()
                .hasSameSizeAs(dbAuthors)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void findAllByIdShouldNotGetAuthorsIfTheyNotAvailable() {
        Set<ObjectId> ids = Sets.newLinkedHashSet(new ObjectId(), new ObjectId());
        List<Author> authors = authorRepository.findAllById(ids);

        assertThat(authors)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void deleteAllShouldDeleteAllAuthors() {
        List<Author> authorsBefore = authorRepository.findAll();
        assertThat(authorsBefore)
                .isNotNull()
                .isNotEmpty();

        authorRepository.deleteAll();

        List<Author> authorsAfter = authorRepository.findAll();
        assertThat(authorsAfter)
                .isNotNull()
                .isEmpty();
    }
}
