package ru.shishmakov.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.shishmakov.domain.Book;

import javax.persistence.EntityNotFoundException;

@RequiredArgsConstructor
@Controller
public class MvcController {
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
        Book book = bookService.getById(id).orElseThrow(() -> new EntityNotFoundException("book:" + id + " not found"));
        model.addAttribute("book", book);
        return "edit";
    }

    @PostMapping("/edit")
    public String editBook(Model model, @ModelAttribute Book book, @RequestParam("id") long id) {
        bookService.update(id, book);
        model.addAttribute("books", bookService.getAll());
        return "books";
    }
}
