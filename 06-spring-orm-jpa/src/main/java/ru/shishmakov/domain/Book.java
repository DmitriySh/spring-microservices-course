package ru.shishmakov.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Entity
public class Book {

    @Id
    @SequenceGenerator(name = "b_seq", sequenceName = "book_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "b_seq", strategy = SEQUENCE)
    private Long id;

    @EqualsAndHashCode.Include
    private String title;
    @NaturalId
    @EqualsAndHashCode.Include
    private String isbn;

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(cascade = {PERSIST, MERGE}, fetch = LAZY)
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private Set<Author> authors = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @ManyToMany(cascade = {PERSIST, MERGE}, fetch = LAZY)
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    private Set<Genre> genres = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "book", cascade = ALL, orphanRemoval = true, fetch = LAZY)
    private Set<Comment> comments = new HashSet<>();

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getBooks().remove(this);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setBook(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setBook(null);
    }

    public void removeAllComment() {
        for (Iterator<Comment> iterator = comments.iterator(); iterator.hasNext(); ) {
            Comment comment = iterator.next();
            comment.setBook(null);
            iterator.remove();
        }
    }
}
