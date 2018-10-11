package ru.shishmakov.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleCommonError(HttpServletRequest req, Exception ex) {
        log.error("Request: {} raised {}", req.getRequestURL(), ex);

        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", ex);
        mv.addObject("url", req.getRequestURL());
        mv.setViewName("error");
        return mv;
    }
}
