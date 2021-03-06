package ru.shishmakov.service;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.shishmakov.model.Question;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class ReaderFileImpl implements Reader {
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final String source;
    private final List<Question> questions = new ArrayList<>();

    public ReaderFileImpl(String source) {
        this.source = source;
    }

    @Override
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Read csv file:<br/>
     * 0 - question<br/>
     * 1 - right answer<br/>
     * 2..n - wrong answers<br/>
     */
    public void init() throws IOException {
        logger.info("init questions...");
        int count = 0;
        Random random = new Random();

        MappingIterator<String[]> iterator = buildIterator();
        while (iterator.hasNext()) {
            String[] values = iterator.next();
            int answer = random.nextInt(values.length - 2);
            List<String> answers = Stream.of(values)
                    .skip(2)
                    .collect(collectingAndThen(toList(), list -> {
                        Collections.shuffle(list);
                        list.add(answer, values[1]);
                        return list;
                    }));
            questions.add(new Question(values[0], answers, answer + 1));
            count += 1;
        }
        logger.info("read csv file: {}, lines: {}", source, count);
    }

    private MappingIterator<String[]> buildIterator() throws IOException {
        return new CsvMapper()
                .enable(CsvParser.Feature.WRAP_AS_ARRAY)
                .readerFor(String[].class)
                .with(CsvSchema.emptySchema().withoutEscapeChar().withColumnSeparator(';'))
                .readValues(this.getClass().getClassLoader().getResourceAsStream(source));
    }
}
