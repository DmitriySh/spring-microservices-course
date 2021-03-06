package ru.shishmakov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.util.StringUtils.concatenateStringArrays;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        String[] commands = {"--spring.shell.command.quit.enabled=false", "--spring.shell.command.script.enabled=false"};
        SpringApplication.run(Main.class, concatenateStringArrays(args, commands));
    }
}
