package ru.shishmakov.repository;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test JPA layer without Web
 */
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@Transactional(propagation = NOT_SUPPORTED)
public class GenreRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private GenreRepository genreRepository;

//    @Test
//    public void getAllShouldGetAllGenres() {
//        List<Genre> genres = genreRepository.findAll();
//
//        assertThat(genres)
//                .isNotNull()
//                .hasSize(3)
//                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
//    }
//
//    @Test
//    public void getByIdShouldGetGenre() {
//        Optional<Genre> genre = genreRepository.findById(1L);
//
//        assertThat(genre)
//                .isNotNull()
//                .isPresent()
//                .get()
//                .hasNoNullFieldsOrProperties();
//    }
//
//    @Test
//    public void createGenreShouldThrowExceptionIfFullnameNull() {
//        assertThatThrownBy(() -> genreRepository.saveAndFlush(Genre.builder().name(null).build()))
//                .isInstanceOf(DataIntegrityViolationException.class)
//                .hasMessageContaining("could not execute statement; SQL [n/a]; constraint [null]; nested exception");
//    }
}
