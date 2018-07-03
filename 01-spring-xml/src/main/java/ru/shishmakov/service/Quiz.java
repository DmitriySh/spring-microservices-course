package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.shishmakov.model.Question;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class Quiz {

    private final Reader reader;

    public void start() {
        log.info("Start quiz...");

        List<Question> questions = reader.getQuestions();
        boolean next = true;
        while (next) {
            questions.forEach(System.out::println);
            next = false;
        }
    }
}
