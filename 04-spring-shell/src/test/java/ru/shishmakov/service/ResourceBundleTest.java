package ru.shishmakov.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import ru.shishmakov.Main.ApplicationProperties;
import ru.shishmakov.model.Question;

import java.util.List;
import java.util.Locale;

import static java.lang.Character.UnicodeBlock.*;
import static java.util.Locale.ENGLISH;
import static org.junit.Assert.*;
import static org.mockito.Mockito.doReturn;

@RunWith(MockitoJUnitRunner.class)
public class ResourceBundleTest {

    @Mock
    private MessageSource messageSource;
    @Mock
    private ApplicationProperties properties;

    @Test
    public void readerShouldHaveRussianLocal() {
        doReturn("RU").when(properties).getLocal();

        Reader reader = new Reader(messageSource, properties);

        assertEquals("Locales are not equal", new Locale("ru", "RU"), reader.getLocal());
    }

    @Test
    public void readerShouldHaveEnglishLocal() {
        doReturn("EN").doReturn("default local").when(properties).getLocal();

        Reader reader1 = new Reader(messageSource, properties);
        Reader reader2 = new Reader(messageSource, properties);

        assertEquals("Locales are not equal", ENGLISH, reader1.getLocal());
        assertEquals("Locales are not equal", ENGLISH, reader2.getLocal());
    }

    @Test
    public void readerShouldHaveRussianQuestions() {
        doReturn("RU").when(properties).getLocal();

        Reader reader = new Reader(buildRealMessageSource(), properties);
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
        doReturn("default local").when(properties).getLocal();

        Reader reader = new Reader(buildRealMessageSource(), properties);
        reader.init();
        List<Question> questions = reader.getQuestions();
        assertFalse("Questions should be", questions.isEmpty());

        String title = questions.get(0).getTitle();
        String answer = questions.get(0).getAnswers().get(0);
        assertTrue("Symbols aren't english", isUnicodeBlock(title, BASIC_LATIN, LATIN_1_SUPPLEMENT, LATIN_EXTENDED_A));
        assertTrue("Symbols aren't english", isUnicodeBlock(answer, BASIC_LATIN, LATIN_1_SUPPLEMENT, LATIN_EXTENDED_A));
    }

    private static boolean isUnicodeBlock(String text, Character.UnicodeBlock... blocks) {
        for (char ch : text.toCharArray()) {
            for (Character.UnicodeBlock block : blocks) {
                if (Character.UnicodeBlock.of(ch) == block) return true;
            }
        }
        return false;
    }

    private ReloadableResourceBundleMessageSource buildRealMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("/i18n/bundle");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
