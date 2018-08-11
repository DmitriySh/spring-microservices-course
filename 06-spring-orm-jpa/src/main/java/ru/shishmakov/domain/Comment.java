package ru.shishmakov.domain;

import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.Instant;

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
@Table(name = "book_comment")
public class Comment {

    @Id
    @SequenceGenerator(name = "c_seq", sequenceName = "book_comment_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "c_seq", strategy = SEQUENCE)
    private Long id;

    @EqualsAndHashCode.Include
    private String text;

    @EqualsAndHashCode.Include
    @NaturalId
    @Basic
    private Instant createDate;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}
