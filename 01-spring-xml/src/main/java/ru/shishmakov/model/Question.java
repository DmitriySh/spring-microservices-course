package ru.shishmakov.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class Question {
    private final String text;
    private final List<String> answers;
    private final Integer trueAnswer;
}
