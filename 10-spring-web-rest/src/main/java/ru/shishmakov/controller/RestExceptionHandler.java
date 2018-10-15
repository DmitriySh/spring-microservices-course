package ru.shishmakov.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<?> entityNotFoundException(HttpServletRequest req, EntityNotFoundException ex) {
        log.error("Request: {} raised {}", req.getRequestURL(), ex.getMessage(), ex);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> commonError(HttpServletRequest req, Exception ex) {
        log.error("Request: {} raised {}", req.getRequestURL(), ex.getMessage());
        return new ResponseEntity<>(INTERNAL_SERVER_ERROR);
    }
}
