package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static java.util.Optional.ofNullable;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Document
public class Book {

    @Id
    @EqualsAndHashCode.Include
    private ObjectId _id;

    @EqualsAndHashCode.Include
    private String title;

    @EqualsAndHashCode.Include
    private String isbn;

    @ToString.Exclude
    @Builder.Default
    @DBRef(lazy = true)
    private Set<Author> authors = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    @DBRef(lazy = true)
    private Set<Genre> genres = new HashSet<>();

    @ToString.Exclude
    @Builder.Default
    private Set<Comment> comments = new HashSet<>();

    public Book addAuthors(Collection<Author> authors) {
        this.authors.addAll(authors);
        authors.forEach(a -> ofNullable(a.getBooks()).map(b -> b.add(this)));
        return this;
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    public void removeAllAuthors() {
        for (Iterator<Author> iterator = authors.iterator(); iterator.hasNext(); ) {
            Author author = iterator.next();
            author.getBooks().remove(this);
            iterator.remove();
        }
    }

    public Book addGenres(Collection<Genre> genres) {
        this.genres.addAll(genres);
        genres.forEach(g -> ofNullable(g.getBooks()).map(b -> b.add(this)));
        return this;
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getBooks().remove(this);
    }

    public void removeAllGenres() {
        for (Iterator<Genre> iterator = genres.iterator(); iterator.hasNext(); ) {
            Genre genre = iterator.next();
            genre.getBooks().remove(this);
            iterator.remove();
        }
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
    }

    public void removeAllComment() {
        comments.clear();
    }
}
