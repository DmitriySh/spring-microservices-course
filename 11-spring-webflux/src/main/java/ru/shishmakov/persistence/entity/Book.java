package ru.shishmakov.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;


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
    private ObjectId id;

    @EqualsAndHashCode.Include
    @NotEmpty
    private String title;

    @EqualsAndHashCode.Include
    @NotEmpty
    @Indexed(unique = true)
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
        authors.forEach(a -> {
            this.authors.add(a);
            a.getBooks().add(this);
        });
        return this;
    }

    public Book addAuthor(Author author) {
        this.authors.add(author);
        author.getBooks().add(this);
        return this;
    }

    public void removeAuthor(Author author) {
        authors.remove(author);
        author.getBooks().remove(this);
    }

    public Set<Author> removeAllAuthors() {
        Set<Author> temp = new HashSet<>();
        for (Iterator<Author> iterator = authors.iterator(); iterator.hasNext(); ) {
            Author author = iterator.next();
            author.getBooks().remove(this);
            iterator.remove();
            temp.add(author);
        }
        return temp;
    }

    public Book addGenres(Collection<Genre> genres) {
        genres.forEach(g -> {
            this.genres.add(g);
            g.getBooks().add(this);
        });
        return this;
    }

    public Book addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getBooks().add(this);
        return this;
    }

    public void removeGenre(Genre genre) {
        genres.remove(genre);
        genre.getBooks().remove(this);
    }

    public Set<Genre> removeAllGenres() {
        Set<Genre> temp = new HashSet<>();
        for (Iterator<Genre> iterator = genres.iterator(); iterator.hasNext(); ) {
            Genre genre = iterator.next();
            genre.getBooks().remove(this);
            iterator.remove();
            temp.add(genre);
        }
        return temp;
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
    }

    public void addComments(Collection<Comment> comments) {
        this.comments.addAll(comments);
    }

    public boolean removeComment(ObjectId commentId) {
        return comments.removeIf(c -> Objects.equals(c.getId(), commentId));
    }

    public void removeAllComments() {
        comments.clear();
    }
}
