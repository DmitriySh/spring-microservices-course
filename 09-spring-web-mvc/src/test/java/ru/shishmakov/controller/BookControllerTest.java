package ru.shishmakov.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shishmakov.domain.Book;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @Test
    public void getRootPageShouldRedirectToBooks() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"))
                .andExpect(content().string(isEmptyString()));
    }

    @Test
    public void getBooksShouldGetBooksPage() throws Exception {
        doReturn(Arrays.asList(
                new Book(1L, "title 1", "isbn 1"),
                new Book(2L, "title 2", "isbn 2"))).when(bookService).getAll();

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(not(isEmptyString())))
                .andExpect(xpath("html/body/table/tbody/tr").nodeCount(is(2)));

        verify(bookService).getAll();
    }

    @Test
    public void getBookEditShouldGetCreationPage() throws Exception {
        mockMvc.perform(get("/book/edit").param("create", "true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(not(isEmptyString())))
                .andExpect(xpath("html/body/span/form/h1").string("Create Book"));
    }

    @Test
    public void getBookEditShouldGetEditPage() throws Exception {
        doReturn(new Book(1L, "title 1", "isbn 1")).when(bookService).getById(anyLong());

        mockMvc.perform(get("/book/edit").param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(content().string(not(isEmptyString())))
                .andExpect(xpath("html/body/span/form/h1").string("Edit Book"));
    }

    @Test
    public void postBookEditShouldUpdateBookAndRedirectToAllBooksPage() throws Exception {
        mockMvc.perform(post("/book/edit")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("title", "title 1")
                .param("isbn", "isbn 1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"))
                .andExpect(content().string(isEmptyString()));

        verify(bookService).update(any(Book.class));
    }

    @Test
    public void postBookInsertShouldCreateNewBookAndRedirectToAllBooksPage() throws Exception {
        mockMvc.perform(post("/book/insert")
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("title", "title 1")
                .param("isbn", "isbn 1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"))
                .andExpect(content().string(isEmptyString()));

        verify(bookService).create(any(Book.class));
    }

    @Test
    public void getBookDeleteShouldRemoveBookAndRedirectToAllBooksPage() throws Exception {
        mockMvc.perform(get("/book/delete")
                .param("id", "1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"))
                .andExpect(content().string(isEmptyString()));

        verify(bookService).delete(eq(1L));
    }
}
