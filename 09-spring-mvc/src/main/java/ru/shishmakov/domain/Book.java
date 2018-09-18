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
import javax.persistence.SequenceGenerator;

import static javax.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
@Entity
public class Book {

    @Id
    @SequenceGenerator(name = "b_seq", sequenceName = "book_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "b_seq", strategy = SEQUENCE)
    private Long id;

    @EqualsAndHashCode.Include
    private String title;

    @NaturalId
    @EqualsAndHashCode.Include
    private String isbn;
}
