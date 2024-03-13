package pl.kurs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.kurs.Main;
import pl.kurs.model.Book;
import pl.kurs.model.command.CreateBookCommand;
import pl.kurs.model.command.EditBookCommand;
import pl.kurs.repository.BookRepository;

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
    private BookRepository bookRepository;

    @Test
    public void shouldReturnSingleBook() throws Exception {
        int id = bookRepository.saveAndFlush(new Book("Pan Tadeusz", "LEKTURA", true)).getId();
        postman.perform(get("/api/v1/books/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.title").value("Pan Tadeusz"))
                .andExpect(jsonPath("$.category").value("LEKTURA"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    public void shouldAddBook() throws Exception {

        CreateBookCommand command = new CreateBookCommand("testowa", "TESTOWA");
        String json = objectMapper.writeValueAsString(command);

        String responseJson = postman.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("testowa"))
                .andExpect(jsonPath("$.category").value("TESTOWA"))
                .andExpect(jsonPath("$.available").value(true))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Book saved = objectMapper.readValue(responseJson, Book.class);

        Book recentlyAdded = bookRepository.findById(saved.getId()).get();

        Assertions.assertEquals("testowa", recentlyAdded.getTitle());
        Assertions.assertEquals("TESTOWA", recentlyAdded.getCategory());
        Assertions.assertTrue(recentlyAdded.isAvailable());
    }

    // napisac testy do pozostałych metod
    @Test
    public void shouldDeleteBook() throws Exception {
        int bookId = bookRepository.saveAndFlush(new Book("testowa", "TESTOWA", true)).getId();

        postman.perform(delete("/api/v1/books/" + bookId))
                .andExpect(status().isNoContent());

        postman.perform(get("/api/v1/books/" + bookId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldEditBook() throws Exception {
        int bookId = bookRepository.saveAndFlush(new Book("testowa", "Testowa", true)).getId();

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

        Book editBook = bookRepository.findById(bookId).get();
        assertNotNull(editBook);
        assertEquals("new", editBook.getTitle());
        assertEquals("NEW", editBook.getCategory());
        assertFalse(editBook.isAvailable());
    }

    @Test
    public void shouldEditBookPartially() throws Exception {

        int bookId = bookRepository.saveAndFlush(new Book("Pan Tadeusz", "LEKTURA", true)).getId();

        EditBookCommand command = new EditBookCommand("NEW", null, false);
        String json = objectMapper.writeValueAsString(command);

        postman.perform(patch("/api/v1/books/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        Book newBook = bookRepository.findById(bookId).get();

        assertEquals("NEW", newBook.getTitle());
        assertEquals("LEKTURA", newBook.getCategory());
        assertFalse(newBook.isAvailable());
    }
}