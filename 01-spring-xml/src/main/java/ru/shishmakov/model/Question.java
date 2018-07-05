package ru.shishmakov.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;
import java.util.Objects;

public class Question {
    private final String text;
    private final List<String> answers;
    private final Integer trueAnswer;

    public Question(String text, List<String> answers, Integer trueAnswer) {
        this.text = text;
        this.answers = answers;
        this.trueAnswer = trueAnswer;
    }

    public String getText() {
        return text;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public boolean checkAnswer(Integer userAnswer) {
        return Objects.equals(trueAnswer, userAnswer);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("text", text)
                .append("answers", answers)
                .append("trueAnswer", trueAnswer)
                .toString();
    }
}
