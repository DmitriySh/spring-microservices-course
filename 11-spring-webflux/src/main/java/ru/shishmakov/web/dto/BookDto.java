package ru.shishmakov.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import ru.shishmakov.persistence.entity.Author;
import ru.shishmakov.persistence.entity.Book;
import ru.shishmakov.persistence.entity.Genre;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(Include.NON_EMPTY)
public class BookDto {
    private ObjectId id;
    private String title;
    private String isbn;
    @Builder.Default
    private Set<ObjectId> authors = new HashSet<>();
    @Builder.Default
    private Set<ObjectId> genres = new HashSet<>();

    public static BookDto from(Book book) {
        return BookDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .authors(book.getAuthors().stream().map(Author::getId).collect(toSet()))
                .genres(book.getGenres().stream().map(Genre::getId).collect(toSet()))
                .build();
    }
}
