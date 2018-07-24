package ru.shishmakov;

import lombok.Getter;
import lombok.Setter;
import org.jline.utils.AttributedString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;
import ru.shishmakov.model.State;
import ru.shishmakov.service.Reader;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static org.springframework.util.StringUtils.concatenateStringArrays;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        String[] commands = {"--spring.shell.command.quit.enabled=false", "--spring.shell.command.script.enabled=false"};
        try (ConfigurableApplicationContext context = SpringApplication.run(Main.class, concatenateStringArrays(args, commands))) {
            // nothing
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

    @Bean
    @Order(HIGHEST_PRECEDENCE)
    public PromptProvider promptProvider(Reader reader) {
        return () -> new AttributedString(reader.getMessage("quiz.input") + "> ");
    }

    @Getter
    @Setter
    @Component
    @ConfigurationProperties("application")
    public static class ApplicationProperties {
        private String local;
        private State state;
    }
}
