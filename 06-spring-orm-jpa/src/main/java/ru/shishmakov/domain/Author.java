package ru.shishmakov.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
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

    @EqualsAndHashCode.Include
    @NaturalId
    private String fullname;

    @ToString.Exclude
    @ManyToMany(mappedBy = "authors", fetch = LAZY)
    private Set<Book> books = new HashSet<>();
}
