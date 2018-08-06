package ru.shishmakov.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.IDENTITY;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Setter
@Getter
@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NaturalId
    @EqualsAndHashCode.Include
    private String fullname;

    @ManyToMany(mappedBy = "authors", cascade = {PERSIST, MERGE})
    private Set<Book> books = new HashSet<>();
}
