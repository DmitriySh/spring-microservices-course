package ru.shishmakov.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
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
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = "book_author",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<Author> authors = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(cascade = {PERSIST, MERGE})
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getBooks().remove(this);
    }
}
