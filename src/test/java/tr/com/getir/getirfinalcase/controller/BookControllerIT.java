package tr.com.getir.getirfinalcase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookUpdateRequest;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.security.CustomUserDetails;
import tr.com.getir.getirfinalcase.security.JwtUtil;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private JwtUtil jwtUtil;

    private String jwtToken;
    private Long bookId;

    /**
     * Sets up a librarian user, generates a JWT token,
     * and inserts a test book before each test.
     */
    @BeforeEach
    void setup() throws Exception {

        userRepository.deleteAll();
        bookRepository.deleteAll();

        User librarian = User.builder()
                .name("Lib")
                .surname("Rarian")
                .email("lib@example.com")
                .password(new BCryptPasswordEncoder().encode("123456789"))
                .phoneNumber("05001112233")
                .userRole(UserRole.LIBRARIAN)
                .build();

        librarian = userRepository.save(librarian);
        jwtToken = "Bearer " + jwtUtil.generateToken(new CustomUserDetails(librarian));

        // Inserting a sample book with a unique ISBN
        String uniqueIsbn = "978-" + UUID.randomUUID();

        BookCreateRequest request = new BookCreateRequest(
                "Clean Code",
                "Robert C. Martin",
                uniqueIsbn,
                "Prentice Hall",
                BookGenre.TECHNOLOGY,
                LocalDate.of(2008, 8, 11),
                "A1"
        );

        mockMvc.perform(post("/api/v1/books/")
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        bookId = bookRepository.findAll().get(0).getId();
    }

    // Should return book details when requested by ID with a valid token
    @Test
    void shouldGetBookById() throws Exception {
        mockMvc.perform(get("/api/v1/books/" + bookId)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.title").value("Clean Code"));
    }

    // Should return a list of all books with pagination
    @Test
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(get("/api/v1/books/")
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.content").isArray());
    }

    // Should return matching books when searched by title
    @Test
    void shouldSearchBooks() throws Exception {
        mockMvc.perform(get("/api/v1/books/search")
                        .header("Authorization", jwtToken)
                        .param("title", "Clean")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.content[0].title").value("Clean Code"));
    }

    // Should update the book title and return a success response
    @Test
    void shouldUpdateBook() throws Exception {
        var updateRequest = new BookUpdateRequest(
                "Clean Code Updated",
                null,
                null,
                null,
                null,
                null,
                null
        );

        mockMvc.perform(patch("/api/v1/books/" + bookId)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // Should delete the book and return a success response
    @Test
    void shouldDeleteBook() throws Exception {
        mockMvc.perform(delete("/api/v1/books/" + bookId)
                        .header("Authorization", jwtToken))
                .andExpect(status().isOk());
    }

    // Should reject unauthenticated requests with 403 Forbidden
    @Test
    void shouldRejectAccessWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/books/" + bookId))
                .andExpect(status().isForbidden());
    }
}
