package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private long id;
    private String title;
    private List<Author> authors;
    private List<Genre> genres;
}
