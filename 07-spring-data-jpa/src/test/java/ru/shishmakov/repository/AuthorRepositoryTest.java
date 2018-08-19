package ru.shishmakov.repository;

import org.assertj.core.util.Sets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Author;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class AuthorRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void getAllShouldGetAllAuthors() {
        List<Author> authors = authorRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(4)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldGetAuthorsIfTheyAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(1L, 2L);
        List<Author> authors = authorRepository.findAllById(ids);

        assertThat(authors)
                .isNotNull()
                .hasSize(ids.size())
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldNotGetAuthorsIfTheyNotAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(101L, 202L);
        List<Author> authors = authorRepository.findAllById(ids);

        assertThat(authors)
                .isNotNull()
                .isEmpty();
    }
}
