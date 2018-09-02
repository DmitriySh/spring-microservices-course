package ru.shishmakov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        String[] commands = {"--spring.shell.command.quit.enabled=false", "--spring.shell.command.script.enabled=false"};
        SpringApplication.run(Main.class, commands);
    }
}
