package ru.shishmakov.dao;

import org.assertj.core.util.Sets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyMap;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CommentRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @SpyBean
    private CommentRepository commentRepository;
    @SpyBean
    private BookRepository bookRepository;

    @Test
    public void getAllShouldGetAllComments() {
        List<Comment> authors = commentRepository.getAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(6)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldGetCommentsIfTheyAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(1L, 2L);
        List<Comment> authors = commentRepository.getByIds(ids);

        assertThat(authors)
                .isNotNull()
                .hasSize(ids.size())
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldNotGetCommentsIfTheyNotAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(101L, 202L);
        List<Comment> authors = commentRepository.getByIds(ids);

        assertThat(authors)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void getByIdShouldGetComment() {
        Optional<Comment> comment = commentRepository.getById(1L, emptyMap());

        assertThat(comment)
                .isNotNull()
                .isPresent()
                .get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    public void saveShouldSaveNewComment() {
        Book book = requireNonNull(bookRepository.getById(1L, emptyMap()).orElse(null));
        Comment comment = Comment.builder().createDate(Instant.now()).text("next comment").build();
        commentRepository.save(comment, book);

        assertThat(comment.getId())
                .isNotNull()
                .isPositive();
        assertThat(comment.getBook())
                .isNotNull()
                .isEqualTo(book);
        assertThat(book.getComments())
                .isNotNull()
                .contains(comment);
    }

    @Test
    public void deleteShouldDeleteComment() {
        Book book = requireNonNull(bookRepository.getById(1L, emptyMap()).orElse(null));
        Comment newComment = Comment.builder().createDate(Instant.now()).text("next comment").build();
        commentRepository.save(newComment, book);

        Long newCommentId = requireNonNull(newComment.getId());

        commentRepository.delete(newComment);
        Optional<Comment> deletedComment = commentRepository.getById(newCommentId, emptyMap());

        assertThat(deletedComment)
                .isNotNull()
                .isNotPresent();
        assertThat(book.getComments())
                .isNotNull()
                .doesNotContain(newComment);
    }
}
