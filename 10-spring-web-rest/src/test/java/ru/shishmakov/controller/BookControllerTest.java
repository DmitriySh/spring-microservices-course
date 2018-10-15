package ru.shishmakov.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.shishmakov.domain.Book;
import ru.shishmakov.dto.BookDto;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private LibraryService libraryService;

    private JacksonTester<Object> json;

    @Before
    public void setup() {
        ObjectMapper mapper = new ObjectMapper();
        JacksonTester.initFields(this, mapper);
    }

    @Test
    public void getRootShouldRedirectToBooks() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/books"))
                .andExpect(content().string(equalTo("RESTfull API for Book Library")));
    }

    @Test
    public void getBooksShouldGetAllBooks() throws Exception {
        doReturn(Arrays.asList(
                Book.builder().id(1L).title("title 1").isbn("isbn 1").build(),
                Book.builder().id(2L).title("title 2").isbn("isbn 2").build()))
                .when(libraryService).getAllBooks();

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(not(isEmptyString())))
                .andExpect(jsonPath("$[*]").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(libraryService).getAllBooks();
    }

    @Test
    public void getBookByIdShouldGetBook() throws Exception {
        doReturn(Book.builder().id(1L).title("title 1").isbn("isbn 1").build())
                .when(libraryService).getBookById(eq(1L));

        mockMvc.perform(get("/book/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(content().string(not(isEmptyString())))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", equalTo("title 1")))
                .andExpect(jsonPath("$.isbn", equalTo("isbn 1")));

        verify(libraryService).getBookById(eq(1L));
    }


    @Test
    public void postBookShouldCreateNewBook() throws Exception {
        Book book = Book.builder().id(1L).title("title 1").isbn("isbn 1").build();
        doReturn(book).when(libraryService).createBook(any(BookDto.class));

        mockMvc.perform(post("/book")
                .contentType(APPLICATION_JSON)
                .content(json.write(BookDto.from(book)).getJson()))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", equalTo("title 1")))
                .andExpect(jsonPath("$.isbn", equalTo("isbn 1")));

        verify(libraryService).createBook(any(BookDto.class));
    }

    @Test
    public void putBookByIdShouldUpdateBook() throws Exception {
        Book book = Book.builder().id(1L).title("title 1").isbn("isbn 1").build();
        doReturn(book).when(libraryService).updateBook(any(BookDto.class));

        mockMvc.perform(put("/book/{id}", 1)
                .contentType(APPLICATION_JSON)
                .content(json.write(BookDto.from(book)).getJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", equalTo("title 1")))
                .andExpect(jsonPath("$.isbn", equalTo("isbn 1")));

        verify(libraryService).updateBook(any(BookDto.class));
    }

    @Test
    public void deleteBookShouldRemoveBook() throws Exception {
        mockMvc.perform(delete("/book/{id}", 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(isEmptyString()));

        verify(libraryService).deleteBookById(eq(1L));
    }
}
