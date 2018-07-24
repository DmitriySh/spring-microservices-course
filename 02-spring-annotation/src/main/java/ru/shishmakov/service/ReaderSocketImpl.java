package ru.shishmakov.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.shishmakov.model.Question;

import java.util.List;

/**
 * Not implement yet.<br/>
 * Use {@link ReaderFileImpl} instead
 */
@Service
public class ReaderSocketImpl implements Reader {
    private final String source;

    public ReaderSocketImpl(@Value("${url:default}") String source) {
        this.source = source;
    }

    @Override
    public String getMessage(String key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Question> getQuestions() {
        throw new UnsupportedOperationException();
    }
}
