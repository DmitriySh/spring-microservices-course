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
import ru.shishmakov.domain.Genre;

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
public class GenreRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @SpyBean
    private GenreRepository genreRepository;

    @Test
    public void getAllShouldGetAllGenres() {
        List<Genre> authors = genreRepository.getAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(3)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldGetGenresIfTheyAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(1L, 2L);
        List<Genre> genres = genreRepository.getByIds(ids);

        assertThat(genres)
                .isNotNull()
                .hasSize(ids.size())
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldNotGetGenresIfTheyNotAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(101L, 202L);
        List<Genre> genres = genreRepository.getByIds(ids);

        assertThat(genres)
                .isNotNull()
                .isEmpty();
    }
}
