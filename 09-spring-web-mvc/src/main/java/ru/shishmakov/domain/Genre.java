package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
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

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Entity
public class Genre {

    @Id
    @SequenceGenerator(name = "g_seq", sequenceName = "genre_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "g_seq", strategy = SEQUENCE)
    private Long id;

    @NaturalId
    @EqualsAndHashCode.Include
    private String name;

    @ManyToMany(mappedBy = "genres", fetch = LAZY)
    @ToString.Exclude
    private Set<Book> books = new HashSet<>();
}
