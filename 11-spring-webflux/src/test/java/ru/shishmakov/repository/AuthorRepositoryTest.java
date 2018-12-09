package ru.shishmakov.repository;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shishmakov.persistence.repository.AuthorRepository;

/**
 * Test JPA layer without Web
 */
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@Transactional(propagation = NOT_SUPPORTED)
public class AuthorRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private AuthorRepository authorRepository;

//    @Test
//    public void getAllShouldGetAllAuthors() {
//        List<Author> authors = authorRepository.findAll();
//
//        assertThat(authors)
//                .isNotNull()
//                .hasSize(4)
//                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
//    }
//
//    @Test
//    public void getByIdShouldGetAuthor() {
//        Optional<Author> author = authorRepository.findById(1L);
//
//        assertThat(author)
//                .isNotNull()
//                .isPresent()
//                .get()
//                .hasNoNullFieldsOrProperties();
//    }
//
//    @Test
//    public void createAuthorShouldThrowExceptionIfFullnameNull() {
//        assertThatThrownBy(() -> authorRepository.saveAndFlush(Author.builder().fullname(null).build()))
//                .isInstanceOf(DataIntegrityViolationException.class)
//                .hasMessageContaining("could not execute statement; SQL [n/a]; constraint [null]; nested exception");
//    }
}
