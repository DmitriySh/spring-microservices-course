package ru.shishmakov.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.model.Question;

import java.io.BufferedReader;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class QuizTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();

    @SpyBean
    private Reader reader;
    @SpyBean
    private Quiz quiz;

    private List<Question> questions = Arrays.asList(
            new Question("1 + 1 = ?", Arrays.asList("1", "2", "3", "4"), 2),
            new Question("1 * 1 = ?", Arrays.asList("1", "2", "3", "4"), 1));

    @Before
    public void setUp() {
        doReturn(questions).when(reader).getQuestions();
        doNothing().when(reader).init();
    }

    @Test
    public void allAnswersShouldBeRight() {
        doReturn("name").doReturn("surname").doReturn(2).doReturn(1)
                .when(quiz).readAnswer(any(BufferedReader.class), any());

        int actualScore = quiz.start();

        assertEquals("All answers should be right", questions.size(), actualScore);
        verify(quiz, times(4)).readAnswer(any(BufferedReader.class), any());
    }

    @Test
    public void allAnswersShouldBeWrong() {
        doReturn("name").doReturn("surname").doReturn(20).doReturn(10)
                .when(quiz).readAnswer(any(BufferedReader.class), any());

        int actualScore = quiz.start();

        assertEquals("All answers should be wrong", 0, actualScore);
        assertTrue("Questions should be more then right answers", questions.size() > actualScore);
        verify(quiz, times(4)).readAnswer(any(BufferedReader.class), any());
    }

    @Test
    public void oneAnswersShouldBeRight() {
        doReturn("name").doReturn("surname").doReturn(2).doReturn(10)
                .when(quiz).readAnswer(any(BufferedReader.class), any());

        int actualScore = quiz.start();

        assertEquals("Only one answers should be right", 1, actualScore);
        assertTrue("Questions should be more then right answers", questions.size() > actualScore);
        verify(quiz, times(4)).readAnswer(any(BufferedReader.class), any());
    }
}
