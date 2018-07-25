package ru.shishmakov.dao;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD, METHOD, TYPE, PARAMETER, CONSTRUCTOR})
@Retention(RUNTIME)
@Qualifier
public @interface AuthorType {
}
