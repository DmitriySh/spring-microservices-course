package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Builder
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

    @Builder.Default
    @ToString.Exclude
    @ManyToMany(mappedBy = "authors", fetch = LAZY)
    private Set<Book> books = new HashSet<>();
}
