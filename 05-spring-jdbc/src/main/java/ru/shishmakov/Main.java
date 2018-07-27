package ru.shishmakov;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

import static org.springframework.util.StringUtils.concatenateStringArrays;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws SQLException {
//        Console.main(args);
        String[] commands = {"--spring.shell.command.script.enabled=false"};
        SpringApplication.run(Main.class, concatenateStringArrays(args, commands));
    }
}
