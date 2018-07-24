package ru.shishmakov.service;

import ru.shishmakov.model.Question;

import java.util.List;

public interface Reader {
    List<Question> getQuestions();
}
