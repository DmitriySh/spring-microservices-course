package ru.shishmakov.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.model.Question;

import java.io.BufferedReader;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuizTest {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();

    @Mock
    private Reader reader;
    private List<Question> questions = Arrays.asList(
            new Question("1 + 1 = ?", Arrays.asList("1", "2", "3", "4"), 2),
            new Question("1 * 1 = ?", Arrays.asList("1", "2", "3", "4"), 1));

    @Before
    public void setUp() {
        doReturn(questions).when(reader).getQuestions();
    }

    @Test
    public void allAnswersShouldBeRight() {
        Quiz quiz = spy(new Quiz(reader));
        doReturn("name").doReturn("surname").doReturn(2).doReturn(1)
                .when(quiz).readAnswer(any(BufferedReader.class), any());

        int actualScore = quiz.start();

        assertEquals("All answers should be right", questions.size(), actualScore);
        verify(quiz, times(4)).readAnswer(any(BufferedReader.class), any());
    }

    @Test
    public void allAnswersShouldBeWrong() {
        Quiz quiz = spy(new Quiz(reader));
        doReturn("name").doReturn("surname").doReturn(20).doReturn(10)
                .when(quiz).readAnswer(any(BufferedReader.class), any());

        int actualScore = quiz.start();

        assertEquals("All answers should be wrong", 0, actualScore);
        assertTrue("Questions should be more then right answers", questions.size() > actualScore);
        verify(quiz, times(4)).readAnswer(any(BufferedReader.class), any());
    }

    @Test
    public void oneAnswersShouldBeRight() {
        Quiz quiz = spy(new Quiz(reader));
        doReturn("name").doReturn("surname").doReturn(2).doReturn(10)
                .when(quiz).readAnswer(any(BufferedReader.class), any());

        int actualScore = quiz.start();

        assertEquals("Only one answers should be right", 1, actualScore);
        assertTrue("Questions should be more then right answers", questions.size() > actualScore);
        verify(quiz, times(4)).readAnswer(any(BufferedReader.class), any());
    }
}
