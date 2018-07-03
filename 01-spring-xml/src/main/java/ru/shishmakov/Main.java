package ru.shishmakov;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.shishmakov.service.Quiz;

public class Main {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("/context.xml")) {
            context
                    .getBean(Quiz.class)
                    .start();
        }
    }
}
