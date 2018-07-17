package ru.shishmakov;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import ru.shishmakov.service.Quiz;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        try (ConfigurableApplicationContext context = SpringApplication.run(Main.class)) {
            context.getBean(Quiz.class)
                    .init()
                    .start();
        }
    }

    @Bean
    @Primary
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:/i18n/bundle");
        ms.setDefaultEncoding("UTF-8");
        return ms;
    }

    @Getter
    @Setter
    @Component
    @ConfigurationProperties("application")
    public static class Application {
        private String local;
    }
}
