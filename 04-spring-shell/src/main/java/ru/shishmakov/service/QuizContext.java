package ru.shishmakov.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import ru.shishmakov.Main.ApplicationProperties;
import ru.shishmakov.model.State;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;

/**
 * Holds the user state of the quiz
 */
@Service
public class QuizContext {
    private final Map<String, Object> data;
    @Getter
    private final String sep;
    @Getter
    @Setter
    private State state;

    public QuizContext(ApplicationProperties properties) {
        this.state = properties.getState();
        this.sep = System.lineSeparator();
        this.data = Stream.of(new SimpleEntry<>("qid", 0), new SimpleEntry<>("score", 0))
                .collect(toMap(Entry::getKey, Entry::getValue));
    }

    Integer readIntData(String key) {
        return (Integer) data.get(key);

    }


    String readStrData(String key) {
        return (String) data.get(key);
    }

    boolean containsData(String key) {
        return data.containsKey(key);
    }

    String removeStrData(String key) {
        return (String) data.remove(key);
    }

    Integer removeIntData(String key) {
        return (Integer) data.remove(key);
    }

    void incrementIntData(String key) {
        data.merge(key, 1, (a, b) -> Stream.of(a, b).mapToInt(Integer.class::cast).sum());
    }

    void putData(String key, Object value) {
        data.put(key, value);
    }
}
