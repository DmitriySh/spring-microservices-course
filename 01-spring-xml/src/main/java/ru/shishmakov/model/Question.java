package ru.shishmakov.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class Question {
    private final String text;
    private final List<String> answers;
}
