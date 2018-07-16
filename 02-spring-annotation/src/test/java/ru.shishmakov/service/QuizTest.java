package ru.shishmakov.service;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.shishmakov.model.Question;

import java.io.BufferedReader;
import java.lang.Character.UnicodeBlock;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.lang.Character.UnicodeBlock.*;
import static java.util.Locale.ENGLISH;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class QuizTest {

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

    @Test
    public void readerShouldHaveRussianLocal() {
        Reader reader = new Reader(mock(MessageSource.class), "ru");

        assertEquals("Locales are not equal", new Locale("ru", "RU"), reader.getLocal());
    }

    @Test
    public void readerShouldHaveEnglishLocal() {
        Reader reader1 = new Reader(mock(MessageSource.class), "en");
        Reader reader2 = new Reader(mock(MessageSource.class), "default local");

        assertEquals("Locales are not equal", ENGLISH, reader1.getLocal());
        assertEquals("Locales are not equal", ENGLISH, reader2.getLocal());
    }

    @Test
    public void readerShouldHaveRussianQuestions() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/i18n/bundle");
        messageSource.setDefaultEncoding("UTF-8");

        Reader reader = new Reader(messageSource, "ru");
        reader.init();
        List<Question> questions = reader.getQuestions();
        assertFalse("Questions should be", questions.isEmpty());

        String title = questions.get(0).getTitle();
        String answer = questions.get(0).getAnswers().get(0);
        assertTrue("Symbols aren't russian", isUnicodeBlock(title, CYRILLIC, CYRILLIC_EXTENDED_A));
        assertTrue("Symbols aren't russian", isUnicodeBlock(answer, CYRILLIC, CYRILLIC_EXTENDED_A));
    }

    @Test
    public void readerShouldHaveEnglishQuestions() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/i18n/bundle");
        messageSource.setDefaultEncoding("UTF-8");

        Reader reader = new Reader(messageSource, "default local");
        reader.init();
        List<Question> questions = reader.getQuestions();
        assertFalse("Questions should be", questions.isEmpty());

        String title = questions.get(0).getTitle();
        String answer = questions.get(0).getAnswers().get(0);
        assertTrue("Symbols aren't english", isUnicodeBlock(title, BASIC_LATIN, LATIN_1_SUPPLEMENT, LATIN_EXTENDED_A));
        assertTrue("Symbols aren't english", isUnicodeBlock(answer, BASIC_LATIN, LATIN_1_SUPPLEMENT, LATIN_EXTENDED_A));
    }

    public static boolean isUnicodeBlock(String text, UnicodeBlock... blocks) {
        for (char ch : text.toCharArray()) {
            for (UnicodeBlock block : blocks) {
                if (UnicodeBlock.of(ch) == block) return true;
            }
        }
        return false;
    }
}
