package ru.shishmakov.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Setter
@Getter
@Entity
@Table(name = "book_comment")
public class Comment {

    @Id
    @SequenceGenerator(name = "c_seq", sequenceName = "comment_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "c_seq", strategy = SEQUENCE)
    private Long id;

    private String text;

    @ToString.Exclude
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return Objects.equals(id, comment.id) &&
                Objects.equals(text, comment.text);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
