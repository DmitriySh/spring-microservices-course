package ru.shishmakov.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Setter
@Getter
public class Comment {

    @EqualsAndHashCode.Include
    private Long id;

    @EqualsAndHashCode.Include
    private String text;

    @EqualsAndHashCode.Include
    private Instant createDate;
}
