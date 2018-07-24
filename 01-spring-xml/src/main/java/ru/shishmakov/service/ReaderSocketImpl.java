package ru.shishmakov.service;

import ru.shishmakov.model.Question;

import java.util.List;

/**
 * Not implement yet.<br/>
 * Use {@link ReaderFileImpl} instead
 */
public class ReaderSocketImpl implements Reader {
    private final String source;

    public ReaderSocketImpl(String source) {
        this.source = source;
    }

    @Override
    public List<Question> getQuestions() {
        throw new UnsupportedOperationException();
    }

}
