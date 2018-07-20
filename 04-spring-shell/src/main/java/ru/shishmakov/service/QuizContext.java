package ru.shishmakov.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ru.shishmakov.Main.ApplicationProperties;
import ru.shishmakov.model.State;

import javax.validation.constraints.Null;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toMap;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Service
@Scope(SCOPE_PROTOTYPE)
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
        this.data = Stream.of(new AbstractMap.SimpleEntry<>("qid", 0), new AbstractMap.SimpleEntry<>("score", 0))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Null Integer readIntData(String key) {
        try {
            return (Integer) data.get(key);
        } catch (Exception ignored) {
            return null;
        }
    }

    @Null String readStrData(String key) {
        try {
            return (String) data.get(key);
        } catch (Exception ignored) {
            return null;
        }
    }

    boolean containsData(String key) {
        return data.containsKey(key);
    }

    void removeData(String key) {
        data.remove(key);
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
