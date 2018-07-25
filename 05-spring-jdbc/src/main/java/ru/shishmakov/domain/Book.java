package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String title;
    @ToString.Exclude
    private List<Author> authors;
    @ToString.Exclude
    private List<Genre> genres;
}
