package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
public class Comment {

    @Id
    @EqualsAndHashCode.Include
    private ObjectId id;

    @EqualsAndHashCode.Include
    private String text;

    @EqualsAndHashCode.Include
    private Instant createDate;
}
