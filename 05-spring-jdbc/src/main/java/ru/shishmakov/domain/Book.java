package ru.shishmakov.domain;

import lombok.*;

import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Long id;
    private String title;
    @ToString.Exclude
    private Set<Author> authors;
    @ToString.Exclude
    private Set<Genre> genres;
}
