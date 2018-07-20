package ru.shishmakov.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.shishmakov.model.Question;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class QuizTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().muteForSuccessfulTests();

    @MockBean
    private Reader reader;
    @MockBean
    private Shell shell;
    @SpyBean
    private QuizContext context;
    @SpyBean(reset = MockReset.BEFORE)
    private Quiz quiz;

    private List<Question> questions = Arrays.asList(
            new Question("1 + 1 = ?", Arrays.asList("1", "2", "3", "4"), 2),
            new Question("1 * 1 = ?", Arrays.asList("1", "2", "3", "4"), 1));

    @Before
    public void setUp() {
        doReturn("name").doReturn("surname").when(context).readStrData(anyString());
        doReturn(true).when(context).containsData(anyString());
        doReturn(questions).when(reader).getQuestions();
        doNothing().when(quiz).finish();
    }

    @Test
    public void allAnswersShouldBeRight() {
        doReturn(2).doReturn(1).when(context).removeIntData(eq("number"));

        // type answers
        quiz.onCommand();
        quiz.onCommand();

        assertEquals("All answers should be right", questions.size(), (int) context.readIntData("score"));
        verify(context, times(2)).readStrData(anyString());
        verify(context, times(2)).removeIntData(anyString());
    }

    @Test
    public void allAnswersShouldBeWrong() {
        doReturn(20).doReturn(10).when(context).removeIntData(eq("number"));

        // type answers
        quiz.onCommand();
        quiz.onCommand();

        assertEquals("All answers should be wrong", 0, (int) context.readIntData("score"));
        assertTrue("Questions should be more then right answers", questions.size() > context.readIntData("score"));
        verify(context, times(2)).readStrData(anyString());
        verify(context, times(2)).removeIntData(anyString());
    }

    @Test
    public void oneAnswersShouldBeRight() {
        doReturn(2).doReturn(10).when(context).removeIntData(eq("number"));

        // type answers
        quiz.onCommand();
        quiz.onCommand();

        assertEquals("Only one answers should be right", 1, (int) context.readIntData("score"));
        assertTrue("Questions should be more then right answers", questions.size() > context.readIntData("score"));
        verify(context, times(2)).readStrData(anyString());
        verify(context, times(2)).removeIntData(anyString());
    }
}
