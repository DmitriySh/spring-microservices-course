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

    /**
     * Replace of main page
     */
    @GetMapping
    public String main() {
        return "redirect:" + "/books";
    }

    /**
     * List all books
     */
    @GetMapping("/books")
    public String getAllBooks(Model model) {
        model.addAttribute("books", bookService.getAll());
        return "books";
    }

    /**
     * Prepare to create new or edit existing book
     */
    @GetMapping("/book/edit")
    public String editBook(Model model,
                           @RequestParam(name = "id", required = false) Long id,
                           @RequestParam(name = "create", defaultValue = "false") boolean create) {
        if (!create) model.addAttribute("book", bookService.getById(id));
        model.addAttribute("create", create);
        return "book";
    }

    /**
     * Update existing book
     */
    @PostMapping("/book/edit")
    public String editBook(@ModelAttribute Book data) {
        bookService.update(data);
        return "redirect:" + "/books";
    }

    /**
     * Insert new book
     */
    @PostMapping(value = "/book/insert")
    public String insertBook(@ModelAttribute Book data) {
        bookService.create(data);
        return "redirect:" + "/books";
    }

    /**
     * Delete existing book
     */
    @GetMapping(value = "/book/delete")
    public String deleteBook(@RequestParam(name = "id") Long id) {
        bookService.delete(id);
        return "redirect:" + "/books";
    }
}