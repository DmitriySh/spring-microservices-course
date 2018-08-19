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
import ru.shishmakov.domain.Book;
import ru.shishmakov.domain.Comment;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * Test JPA layer without Web
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = NOT_SUPPORTED)
public class CommentRepositoryTest {
    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void getAllShouldGetAllComments() {
        List<Comment> authors = commentRepository.findAll();

        assertThat(authors)
                .isNotNull()
                .hasSize(6)
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldGetCommentsIfTheyAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(1L, 2L);
        List<Comment> authors = commentRepository.findAllById(ids);

        assertThat(authors)
                .isNotNull()
                .hasSize(ids.size())
                .matches(list -> list.stream().allMatch(Objects::nonNull), "all elements are not null");
    }

    @Test
    public void getByIdsShouldNotGetCommentsIfTheyNotAvailable() {
        Set<Long> ids = Sets.newLinkedHashSet(101L, 202L);
        List<Comment> authors = commentRepository.findAllById(ids);

        assertThat(authors)
                .isNotNull()
                .isEmpty();
    }

    @Test
    public void getByIdShouldGetComment() {
        Optional<Comment> comment = commentRepository.findById(1L);

        assertThat(comment)
                .isNotNull()
                .isPresent()
                .get()
                .hasNoNullFieldsOrProperties();
    }

    @Test
    @Transactional
    public void saveShouldSaveNewComment() {
        Book book = requireNonNull(bookRepository.findById(1L).orElse(null));
        Comment comment = Comment.builder().createDate(Instant.now()).text("next comment").build();
        book.addComment(comment);
        commentRepository.save(comment);

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
    @Transactional
    public void deleteShouldDeleteComment() {
        Book book = requireNonNull(bookRepository.findByIdWithFetchComments(1L).orElse(null));
        Comment newComment = Comment.builder().createDate(Instant.now()).text("next comment").build();
        book.addComment(newComment);
        commentRepository.save(newComment);

        Long newCommentId = requireNonNull(newComment.getId());
        assertThat(book.getComments())
                .isNotNull()
                .contains(newComment);

        book.removeComment(newComment);
        commentRepository.delete(newComment);
        Optional<Comment> deletedComment = commentRepository.findById(newCommentId);

        assertThat(deletedComment)
                .isNotNull()
                .isNotPresent();
        assertThat(book.getComments())
                .isNotNull()
                .doesNotContain(newComment);
    }
}
