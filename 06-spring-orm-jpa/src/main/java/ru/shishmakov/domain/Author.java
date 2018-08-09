package ru.shishmakov.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Entity
public class Author {
    @Id
    @SequenceGenerator(name = "a_seq", sequenceName = "author_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "a_seq", strategy = SEQUENCE)
    private Long id;

    @NaturalId
    @EqualsAndHashCode.Include
    private String fullname;

    @ManyToMany(mappedBy = "authors", cascade = {PERSIST, MERGE})
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();
}
