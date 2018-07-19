package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Quit;
import ru.shishmakov.model.Question;
import ru.shishmakov.model.State;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

import static ru.shishmakov.model.State.*;

/**
 * Console client to make the quiz
 */
@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class Quiz implements Quit.Command {
    private final Reader reader;
    private final Map<String, Object> context = new HashMap<>();
    private final String sep = System.lineSeparator();

    private State state = IDLE;

    @PostConstruct
    public void init() {
        reader.init();
        context.putIfAbsent("qid", 0);
        context.putIfAbsent("score", 0);
        System.out.print(sep + reader.getMessage("intro.hello") + sep);
        System.out.print(reader.getMessage("intro.name&surname") + sep + sep);
    }

    @ShellMethod(value = "Number of answer.")
    public void number(int number) {
        if (state == START) context.put("number", number);
        doAfter();
    }

    @ShellMethod(value = "Your name.")
    public void name(String text) {
        context.putIfAbsent("name", text.trim());
        doAfter();
    }

    @ShellMethod(value = "Your surname.")
    public void surname(String text) {
        context.putIfAbsent("surname", text.trim());
        doAfter();
    }

    @ShellMethod(value = "Exit the quiz.", key = {"exit", "quit"})
    public void exit() {
        finish();
    }

    @Null
    private <R> R readAnswer(String key, Function<Object, R> function) {
        try {
            return function.apply(context.get(key));
        } catch (Exception ignored) {
            return null;
        }
    }

    private void doAfter() {
        switch (state) {
            case IDLE: {
                if (context.containsKey("name") && context.containsKey("surname")) {
                    String name = readAnswer("name", String.class::cast);
                    String surname = readAnswer("surname", String.class::cast);
                    System.out.println(String.format("%s %s %s!", reader.getMessage("intro.start.1"), name, surname));
                    System.out.println(reader.getMessage("intro.start.2"));
                    state = START;
                    doAfter();
                } else System.out.print(reader.getMessage("intro.name&surname") + sep);
                break;
            }
            case START: {
                if (context.containsKey("number")) {
                    Integer qid = (Integer) context.put("qid", (Integer) context.get("qid") + 1);
                    Question q = reader.getQuestions().get(qid);
                    if (q.checkAnswer(readAnswer("number", Integer.class::cast))) {
                        context.merge("score", 1, (a, b) -> Stream.of(a, b).mapToInt(Integer.class::cast).sum());
                    }
                    context.remove("number");
                }
                if (Objects.equals(context.get("qid"), reader.getQuestions().size())) {
                    state = FINISH;
                    doAfter();
                } else {
                    Question q = reader.getQuestions().get((Integer) context.get("qid"));
                    System.out.println(String.format("%s%s %s: %s", sep, reader.getMessage("quiz.question"), (Integer) context.get("qid") + 1, q.getTitle()));
                    System.out.println(String.format("%s: %s", reader.getMessage("quiz.answers"), getSolutions(q)));
                    System.out.print(reader.getMessage("quiz.type.answer") + sep);
                }
                break;
            }
            case FINISH:
                finish();
        }
    }

    private void finish() {
        System.out.println(String.format("%s%s: %s/%s", sep, reader.getMessage("quiz.result"), context.get("score"), reader.getQuestions().size()));
        System.out.println(reader.getMessage("quiz.result.bye"));

        System.exit(0);
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
