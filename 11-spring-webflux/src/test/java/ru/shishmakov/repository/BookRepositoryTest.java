package ru.shishmakov.repository;

import org.junit.Rule;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.springframework.beans.factory.annotation.Autowired;
import ru.shishmakov.persistence.repository.BookRepository;

/**
 * Test JPA layer without Web
 */
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@Transactional(propagation = NOT_SUPPORTED)
public class BookRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private BookRepository bookRepository;

//    @Test
//    public void getAllShouldGetAllBooks() {
//        List<Book> books = bookRepository.findAll();
//
//        assertThat(books)
//                .isNotNull()
//                .hasSize(4)
//                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
//    }
//
//    @Test
//    public void getByIdShouldGetBook() {
//        Optional<Book> book = bookRepository.findById(1L);
//
//        assertThat(book)
//                .isNotNull()
//                .isPresent()
//                .get()
//                .hasNoNullFieldsOrProperties();
//    }
//
//    @Test
//    @Transactional
//    public void saveAndFlushShouldSaveNewBook() {
//        Book newBook = Book.builder().title("title").isbn("isbn").build();
//        assertThat(newBook.getId())
//                .isNull();
//
//        Book savedBook = bookRepository.saveAndFlush(newBook);
//        assertThat(savedBook.getId())
//                .isNotNull()
//                .isPositive();
//    }
//
//    @Test
//    public void deleteByIdShouldDeleteBook() {
//        Book book = bookRepository.saveAndFlush(Book.builder().title("title").isbn("isbn").build());
//
//        Long bookId = requireNonNull(book.getId());
//        bookRepository.deleteById(bookId);
//
//        Optional<Book> deletedBook = bookRepository.findById(bookId);
//        assertThat(deletedBook)
//                .isNotNull()
//                .isNotPresent();
//    }
//
//    @Test
//    public void createBookShouldThrowExceptionIfTitleNull() {
//        assertThatThrownBy(() -> bookRepository.saveAndFlush(Book.builder().title(null).isbn(UUID.randomUUID().toString()).build()))
//                .isInstanceOf(DataIntegrityViolationException.class)
//                .hasMessageContaining("could not execute statement; SQL [n/a]; constraint [null]; nested exception");
//    }
//
//    @Test
//    @Transactional
//    public void createBookShouldThrowExceptionIfIsbnIsNotUnique() {
//        assertThatThrownBy(() -> {
//            bookRepository.saveAndFlush(Book.builder().title("title").isbn("not unique isbn").build());
//            bookRepository.saveAndFlush(Book.builder().title("title").isbn("not unique isbn").build());
//        })
//                .isInstanceOf(DataIntegrityViolationException.class)
//                .hasMessageContaining("could not execute statement; SQL [n/a]; constraint");
//    }
}
