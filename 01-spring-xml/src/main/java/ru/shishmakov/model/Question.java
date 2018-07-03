package ru.shishmakov.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@ToString
@RequiredArgsConstructor
public class Question {
    private final String text;
    private final List<String> answers;
    private final Integer trueAnswer;

    public boolean checkAnswer(Integer userAnswer) {
        return Objects.equals(trueAnswer, userAnswer);
    }
}
