package ru.shishmakov;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.shishmakov.service.Quiz;

@Configuration
@ComponentScan
public class Main {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(Main.class)) {
            context.getBean(Quiz.class)
                    .start();
        }
    }
}
