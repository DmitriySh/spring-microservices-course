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
import ru.shishmakov.persistence.entity.Author;

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
public class AuthorRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private AuthorRepository authorRepository;
    private List<Author> dbAuthors;

    @Before
    public void setUp() {
        this.dbAuthors = List.of(
                Author.builder().fullname("author 1").build(),
                Author.builder().fullname("author 2").build(),
                Author.builder().fullname("author 3").build(),
                Author.builder().fullname("author 4").build());
        authorRepository.deleteAll()
                .thenMany(Flux.fromIterable(dbAuthors)
                        .collectList()
                        .flatMapMany(authorRepository::saveAll))
                .then()
                .block();
    }

    @Test
    public void getAllShouldGetAllAuthors() {
        Stream<Author> authors = authorRepository.findAll().toStream();

        assertThat(authors)
                .isNotNull()
                .hasSize(dbAuthors.size())
                .allMatch(Objects::nonNull, "all elements are not null")
                .allMatch(author -> Objects.nonNull(author.getId()), "all ids are initialized");
    }

    @Test
    public void findByIdShouldNotGetAuthorIfNotAvailable() {
        Mono<Author> author = authorRepository.findById(new ObjectId());

        StepVerifier
                .create(author)
                .expectNextCount(0)
                .verifyComplete();
    }


    @Test
    public void findAllByIdShouldGetAuthorsIfTheyAvailable() {
        Set<ObjectId> ids = dbAuthors.stream().map(Author::getId).collect(toSet());
        Flux<Author> authors = authorRepository.findAllById(ids);

        StepVerifier
                .create(authors)
                .expectNextCount(dbAuthors.size())
                .verifyComplete();
    }

    @Test
    public void findAllByIdShouldNotGetAuthorsIfTheyNotAvailable() {
        Set<ObjectId> ids = Set.of(new ObjectId(), new ObjectId());
        Flux<Author> authors = authorRepository.findAllById(ids);

        StepVerifier
                .create(authors)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deleteByIdShouldDeleteAuthor() {
        ObjectId authorId = dbAuthors.get(0).getId();
        Mono<Author> author = authorRepository.findById(authorId);
        StepVerifier
                .create(author)
                .expectNextCount(1)
                .verifyComplete();

        Mono<Author> deletedAuthor = authorRepository.deleteById(authorId)
                .then(authorRepository.findById(authorId));
        StepVerifier
                .create(deletedAuthor)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void deleteAllShouldDeleteAllAuthors() {
        Flux<Author> authorsBefore = authorRepository.findAll();
        StepVerifier
                .create(authorsBefore)
                .expectNextCount(dbAuthors.size())
                .verifyComplete();

        Flux<Author> authorsAfter = authorRepository
                .deleteAll()
                .thenMany(authorRepository.findAll());
        StepVerifier
                .create(authorsAfter)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void createAuthorShouldSucceeded() {
        Mono<Author> savedAuthor = authorRepository.save(Author.builder().fullname("new author").build());

        StepVerifier
                .create(savedAuthor)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void createAuthorShouldThrowExceptionIfFullnameNull() {
        Mono<Author> savedAuthor = authorRepository.save(Author.builder().fullname(null).build());

        StepVerifier
                .create(savedAuthor)
                .expectNextCount(0)
                .expectError()
                .verifyThenAssertThat()
                .hasOperatorErrorOfType(ConstraintViolationException.class)
                .hasOperatorErrorWithMessage("fullname: must not be empty");
    }
}
