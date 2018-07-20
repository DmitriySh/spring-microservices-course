package ru.shishmakov.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.shishmakov.Main.ApplicationProperties;
import ru.shishmakov.model.Question;

import java.util.*;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Slf4j
@Getter
@Service
public class Reader {
    private final MessageSource messages;
    private final Locale local;
    private final List<Question> questions = new ArrayList<>();

    public Reader(MessageSource messages, ApplicationProperties properties) {
        this.messages = messages;
        this.local = buildLocal(properties.getLocal());
    }

    /**
     * Read questions from bundle file file:<br/>
     * title - question<br/>
     * 1st answer - right answer<br/>
     * 2nd .. n - wrong answers<br/>
     */
    public void init() {
        log.info("init questions...");
        int count = 0;
        Random random = new Random();

        for (int i = 1; !Thread.currentThread().isInterrupted(); i++) {
            String title = getMessage("question.title." + i);
            String[] values = getMessage("question.answers." + i).split(";");
            if (isBlank(title) || values.length < 2) break;

            int rightAnswer = random.nextInt(values.length - 1);
            List<String> answers = Stream.of(values)
                    .skip(1)
                    .collect(collectingAndThen(toList(), list -> {
                        Collections.shuffle(list);
                        list.add(rightAnswer, values[0]);
                        return list;
                    }));
            questions.add(new Question(title, answers, rightAnswer + 1));
            count += 1;
        }
        log.info("read questions: {}", count);
    }

    public String getMessage(String key) {
        return messages.getMessage(key, null, EMPTY, local);
    }

    private Locale buildLocal(String local) {
        return Optional.ofNullable(local)
                .map(String::trim)
                .map(String::toLowerCase)
                .map(l -> {
                    switch (l) {
                        case "ru":
                            return Locale.forLanguageTag("ru-RU");
                        default:
                            return Locale.ENGLISH;
                    }
                })
                .orElse(Locale.ENGLISH);
    }
}
