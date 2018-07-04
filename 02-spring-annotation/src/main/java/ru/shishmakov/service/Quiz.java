package ru.shishmakov.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.shishmakov.model.Question;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Console client to make the quiz
 */
@Slf4j
@Service
public class Quiz {

    private final Locale local;
    private final Reader reader;
    private final MessageSource messages;

    public Quiz(Reader reader, MessageSource messages, @Value("${local:en}") String local) {
        this.reader = reader;
        this.messages = messages;
        this.local = buildLocal(local);
    }

    /**
     * Start the quiz
     *
     * @return count of right answers (score)
     */
    public int start() {
        String sep = System.lineSeparator();
        log.info(getMessage("intro.hello") + sep);
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(getMessage("intro.name") + ": ");
            String name = readAnswer(input, str -> str);

            System.out.print(getMessage("intro.surname") + ": ");
            String surname = readAnswer(input, str -> str);

            System.out.println(String.format("%s %s %s!", getMessage("intro.start.1"), name, surname));
            System.out.println(getMessage("intro.start.2"));
            List<Question> questions = reader.getQuestions();
            int score = 0;
            for (int i = 0, length = questions.size(); i < length; i++) {
                Question q = questions.get(i);
                System.out.println(String.format("%s%s %s: %s", sep, getMessage("quiz.question"), i + 1, q.getText()));

                System.out.println(String.format("%s: %s", getMessage("quiz.answers"), getSolutions(q)));
                System.out.print(getMessage("quiz.type.answer") + ": ");
                Integer answer = readAnswer(input, Integer::valueOf);
                if (q.checkAnswer(answer)) score++;
            }

            System.out.println(String.format("%s%s: %s/%s", sep, getMessage("quiz.result"), score, questions.size()));
            System.out.println(getMessage("quiz.result.bye"));
            return score;
        } catch (Exception e) {
            log.error("error at the time of quiz", e);
        }
        return 0;
    }

    String getMessage(String key) {
        return messages.getMessage(key, null, local);
    }

    <R> R readAnswer(BufferedReader input, Function<String, R> function) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final String read = input.readLine();
                if (isNotBlank(read)) return function.apply(trim(read));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    private Locale buildLocal(String local) {
        return Optional.ofNullable(local)
                .map(l -> {
                    switch (l) {
                        case "ru":
                            return new Locale("ru", "RU");
                        default:
                            return Locale.ENGLISH;
                    }
                })
                .orElse(Locale.ENGLISH);
    }

    private String getSolutions(Question q) {
        List<String> answers = q.getAnswers();
        StringBuilder solutions = new StringBuilder();
        for (int j = 0, length = answers.size(); j < length; j++) {
            solutions.append("\n\t").append(j + 1).append(": ").append(answers.get(j));
        }
        return solutions.toString();
    }
}
