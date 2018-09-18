package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shishmakov.domain.Book;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MvcController {
    private final BookService bookService;

    @RequestMapping
    public String hello() {
        return "index";
    }

    @RequestMapping("/books")
    public String getAllBooks(Model model) {
        List<Book> all = bookService.getAll();
        model.addAttribute("books", all);
        return "books";
    }
}
