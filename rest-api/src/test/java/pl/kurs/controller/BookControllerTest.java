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
import pl.kurs.service.BookIdGenerator;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        Assertions.assertTrue( recentlyAdded.isAvailable());

        Mockito.verify(bookIdGenerator, Mockito.times(1)).getId();
    }

    // napisac testy do pozostałych metod
  
}