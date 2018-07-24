package ru.shishmakov.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Quit;
import ru.shishmakov.model.Question;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

import static ru.shishmakov.model.State.FINISH;
import static ru.shishmakov.model.State.RUN;

/**
 * Console client to make the quiz
 */
@Slf4j
@RequiredArgsConstructor
@ShellComponent
public class Quiz implements Quit.Command {
    private final Reader reader;
    private final QuizContext context;

    @PostConstruct
    public void init() {
        reader.init();
        System.out.print(context.getSep() + reader.getMessage("intro.hello") + context.getSep());
        System.out.print(reader.getMessage("intro.name&surname") + context.getSep() + context.getSep());
    }

    @ShellMethod(value = "Number of answer.")
    public void number(int number) {
        if (context.getState() == RUN) context.putData("number", number);
        onCommand();
    }

    @ShellMethod(value = "Your name.")
    public void name(String text) {
        context.putData("name", text.trim());
        onCommand();
    }

    @ShellMethod(value = "Your surname.")
    public void surname(String text) {
        context.putData("surname", text.trim());
        onCommand();
    }

    @ShellMethod(value = "Exit the quiz.", key = {"exit", "quit"})
    public void exit() {
        finish();
    }

    void onCommand() {
        switch (context.getState()) {
            case IDLE:
                processIdle();
                break;
            case RUN:
                processRun();
                break;
            case FINISH:
                finish();
        }
    }

    private void processRun() {
        if (context.containsData("number")) {
            Question q = reader.getQuestions().get(context.readIntData("qid"));
            if (q.checkAnswer(context.removeIntData("number")))
                context.incrementIntData("score");
            context.incrementIntData("qid");
        }
        if (Objects.equals(context.readIntData("qid"), reader.getQuestions().size())) {
            context.setState(FINISH);
            onCommand(); // go on
        } else {
            Question q = reader.getQuestions().get(context.readIntData("qid"));
            System.out.println(String.format("%s%s %s: %s", context.getSep(),
                    reader.getMessage("quiz.question"), context.readIntData("qid") + 1, q.getTitle()));
            System.out.println(String.format("%s: %s", reader.getMessage("quiz.answers"), getSolutions(q)));
            System.out.print(reader.getMessage("quiz.type.answer") + context.getSep());
        }
    }

    private void processIdle() {
        if (context.containsData("name") && context.containsData("surname")) {
            String name = context.readStrData("name");
            String surname = context.readStrData("surname");
            System.out.println(String.format("%s %s %s!", reader.getMessage("intro.start.1"), name, surname));
            System.out.println(reader.getMessage("intro.start.2"));
            context.setState(RUN);
            onCommand(); // go on
        } else System.out.print(reader.getMessage("intro.name&surname") + context.getSep());
    }

    void finish() {
        System.out.println(String.format("%s%s: %s/%s", context.getSep(),
                reader.getMessage("quiz.result"), context.readIntData("score"), reader.getQuestions().size()));
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
