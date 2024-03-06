package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.service.BookIdGenerator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Main.class)
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private List<Book> books;

    @SpyBean
    private BookIdGenerator bookIdGenerator;

    @Test
    public void shouldReturnSingleBook() throws Exception {

        postman.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Ogniem i mieczem"))
                .andExpect(jsonPath("$.category").value("LEKTURA"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void shouldAddBook() throws Exception {

        CreateBookCommand command = new CreateBookCommand("testowa", "TESTOWA");
        String json = objectMapper.writeValueAsString(command);

        postman.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("testowa"))
                .andExpect(jsonPath("$.category").value("TESTOWA"))
                .andExpect(jsonPath("$.available").value(true));

        Book recentlyAdded = books.get(books.size() - 1);

        Assertions.assertEquals("testowa", recentlyAdded.getTitle());
        Assertions.assertEquals("TESTOWA", recentlyAdded.getCategory());
        Assertions.assertTrue(recentlyAdded.isAvailable());

        Mockito.verify(bookIdGenerator, Mockito.times(1)).getId();
    }

    // napisac testy do pozostałych metod

    @Test
    public void shoudFindBook() throws Exception {
        int bookId = 1;

        postman.perform(get("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.category").exists())
                .andExpect(jsonPath("$.available").exists());
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        int bookId = 1;

        postman.perform(delete("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        assertFalse(books.stream().anyMatch(b -> b.getId() == bookId));

    }

    @Test
    public void shouldEditBook() throws Exception {
        int bookId = 1;
        EditBookCommand command = new EditBookCommand("new", "NEW", false);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(put("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").value("new"))
                .andExpect(jsonPath("$.category").value("NEW"))
                .andExpect(jsonPath("$.available").value(false));

        Book newBook = books.stream()
                .filter(book -> book.getId() == bookId)
                .findFirst()
                .orElse(null);
        assertNotNull(newBook);
        assertEquals("new", newBook.getTitle());
        assertEquals("NEW", newBook.getCategory());
        assertFalse(newBook.isAvailable());
    }

    @Test
    public void shouldEditBookPartially() throws Exception {
        int bookId = 1;
        EditBookCommand command = new EditBookCommand(null, "NEW", null);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.category").value("NEW"))
                .andExpect(jsonPath("$.available").exists());

        Book newBook = books.stream()
                .filter(book -> book.getId() == bookId).findFirst().orElse(null);
        assertNotNull(newBook);
        assertEquals("NEW", newBook.getCategory());

    }
}