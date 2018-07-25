package ru.shishmakov.service;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import javax.annotation.PostConstruct;

@ShellComponent
public class Library {

    @PostConstruct
    public void init() {
        System.out.print("\nWelcome to demo Library!\n");
    }

    @ShellMethod(value = "Number of answer.")
    public void number(int number) {

    }
}
