package ru.shishmakov.dao;

import org.assertj.core.util.Sets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shishmakov.domain.Author;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class AuthorRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @SpyBean
    private AuthorRepository authorRepository;

    @Test
    public void getAllShouldGetAllAuthors() {
        List<Author> authors = authorRepository.getAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(4)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldGetAuthorsIfTheyAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(1L, 2L);
        List<Author> authors = authorRepository.getByIds(ids);

        assertThat(authors)
                .isNotNull()
                .hasSize(ids.size())
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldNotGetAuthorsIfTheyNotAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(101L, 202L);
        List<Author> authors = authorRepository.getByIds(ids);

        assertThat(authors)
                .isNotNull()
                .isEmpty();
    }
}
