package ru.shishmakov.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.model.Question;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.*;

/**
 * Console client to make the quiz
 */
public class Quiz {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Reader reader;

    public Quiz(Reader reader) {
        this.reader = reader;
    }

    /**
     * Start the quiz
     *
     * @return count of right answers (score)
     */
    public int start() {
        String sep = System.lineSeparator();
        logger.info("Hello quiz!" + sep);
        try (BufferedReader input = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Please type your first name: ");
            String name = readAnswer(input, str -> str);

            System.out.print("Please type your surname: ");
            String surname = readAnswer(input, str -> str);

            System.out.print(String.format("Good luck %s %s!%sStart quiz...%s", name, surname, sep, sep));
            List<Question> questions = reader.getQuestions();
            int score = 0;
            for (int i = 0, length = questions.size(); i < length; i++) {
                Question q = questions.get(i);
                System.out.println(String.format("%sQuestion %s: %s", sep, i + 1, q.getText()));

                System.out.println(String.format("Answers: %s", getSolutions(q)));
                System.out.print("Please type number your answer: ");
                Integer answer = readAnswer(input, Integer::valueOf);
                if (q.checkAnswer(answer)) score++;
            }

            System.out.println(String.format("%sResult: %s/%s%sByu! =)", sep, score, questions.size(), sep));
            return score;
        } catch (Exception e) {
            logger.error("error at the time of quiz", e);
        }
        return 0;
    }

    private String getSolutions(Question q) {
        List<String> answers = q.getAnswers();
        StringBuilder solutions = new StringBuilder();
        for (int j = 0, length = answers.size(); j < length; j++) {
            solutions.append("\n\t").append(j + 1).append(": ").append(answers.get(j));
        }
        return solutions.toString();
    }

    <R> R readAnswer(BufferedReader input, Function<String, R> function) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                final String read = input.readLine();
                if (isBlank(read)) continue;
                else return function.apply(trim(lowerCase(read)));
            } catch (Exception ignored) {
            }
        }
        return null;
    }
}
