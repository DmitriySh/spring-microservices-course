package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shishmakov.domain.Book;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    @GetMapping
    public String hello() {
        return "index";
    }

    @GetMapping("/books")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "books";
    }

    @GetMapping("/edit")
    public String editBook(Model model, @RequestParam("id") long id) {
        model.addAttribute("book", bookService.getById(id));
        return "edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute Book data, @RequestParam("id") long id) {
        bookService.update(id, data);
        return "redirect:" + "/books";
    }
}
