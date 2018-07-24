package ru.shishmakov.service;

import ru.shishmakov.model.Question;

import java.util.List;

public interface Reader {
    String getMessage(String key);

    List<Question> getQuestions();
}
