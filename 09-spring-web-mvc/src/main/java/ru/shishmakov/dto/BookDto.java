package ru.shishmakov.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class BookDto {
    private Long id;
    private String title;
    private String isbn;
    private Set<Long> authors = new HashSet<>();
    private Set<Long> genres = new HashSet<>();
}
