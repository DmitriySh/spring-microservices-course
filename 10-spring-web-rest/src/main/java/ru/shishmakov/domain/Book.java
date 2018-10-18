package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
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

    @NaturalId(mutable = true)
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

    public void removeAllAuthors() {
        for (Iterator<Author> iterator = authors.iterator(); iterator.hasNext(); ) {
            Author author = iterator.next();
            author.getBooks().remove(this);
            iterator.remove();
        }
    }

    public void removeAllGenres() {
        for (Iterator<Genre> iterator = genres.iterator(); iterator.hasNext(); ) {
            Genre genre = iterator.next();
            genre.getBooks().remove(this);
            iterator.remove();
        }
    }

}
