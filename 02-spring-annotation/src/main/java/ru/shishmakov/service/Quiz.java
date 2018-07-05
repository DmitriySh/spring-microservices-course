package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.shishmakov.model.Question;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Console client to make the quiz
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class Quiz {
    private final Reader reader;

    /**
     * Start the quiz
     *
     * @return count of right answers (score)
     */
    public int start() {
        String sep = System.lineSeparator();
        log.info(reader.getMessage("intro.hello") + sep);
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print(reader.getMessage("intro.name") + ": ");
            String name = readAnswer(input, str -> str);

            System.out.print(reader.getMessage("intro.surname") + ": ");
            String surname = readAnswer(input, str -> str);

            System.out.println(String.format("%s %s %s!", reader.getMessage("intro.start.1"), name, surname));
            System.out.println(reader.getMessage("intro.start.2"));
            List<Question> questions = reader.getQuestions();
            int score = 0;
            for (int i = 0, length = questions.size(); i < length; i++) {
                Question q = questions.get(i);
                System.out.println(String.format("%s%s %s: %s", sep, reader.getMessage("quiz.question"), i + 1, q.getTitle()));

                System.out.println(String.format("%s: %s", reader.getMessage("quiz.answers"), getSolutions(q)));
                System.out.print(reader.getMessage("quiz.type.answer") + ": ");
                Integer answer = readAnswer(input, Integer::valueOf);
                if (q.checkAnswer(answer)) score++;
            }

            System.out.println(String.format("%s%s: %s/%s", sep, reader.getMessage("quiz.result"), score, questions.size()));
            System.out.println(reader.getMessage("quiz.result.bye"));
            return score;
        } catch (Exception e) {
            log.error("error at the time of quiz", e);
        }
        return 0;
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

    private String getSolutions(Question q) {
        List<String> answers = q.getAnswers();
        StringBuilder solutions = new StringBuilder();
        for (int j = 0, length = answers.size(); j < length; j++) {
            solutions.append("\n\t").append(j + 1).append(": ").append(answers.get(j));
        }
        return solutions.toString();
    }
}
