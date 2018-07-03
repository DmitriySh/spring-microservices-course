package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.model.Question;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RequiredArgsConstructor
public class Quiz {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Store store;

    public void start() {
        logger.info("Start quiz...");

        List<Question> questions = store.getQuestions();
        boolean next = true;
        while (next) {

        }
    }
}
