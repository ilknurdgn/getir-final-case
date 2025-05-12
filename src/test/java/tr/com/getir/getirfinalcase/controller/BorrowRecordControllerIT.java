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
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.repository.BorrowRecordRepository;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.security.CustomUserDetails;
import tr.com.getir.getirfinalcase.security.JwtUtil;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BorrowRecordControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private BorrowRecordRepository borrowRecordRepository;
    @Autowired private JwtUtil jwtUtil;

    private String patronToken;
    private String librarianToken;
    private Long bookId;
    private Long patronId;

    @BeforeEach
    void setup() {
        borrowRecordRepository.deleteAll();
        bookRepository.deleteAll();
        userRepository.deleteAll();

        // Create patron user
        User patron = User.builder()
                .name("Ali")
                .surname("Veli")
                .email("patron@example.com")
                .password(new BCryptPasswordEncoder().encode("123456789"))
                .phoneNumber("05000000000")
                .userRole(UserRole.PATRON)
                .build();
        patronId = userRepository.save(patron).getId();
        patronToken = "Bearer " + jwtUtil.generateToken(new CustomUserDetails(patron));

        // Create librarian user
        User librarian = User.builder()
                .name("Zeynep")
                .surname("Kaya")
                .email("librarian@example.com")
                .password(new BCryptPasswordEncoder().encode("123456789"))
                .phoneNumber("05001112233")
                .userRole(UserRole.LIBRARIAN)
                .build();
        librarianToken = "Bearer " + jwtUtil.generateToken(new CustomUserDetails(userRepository.save(librarian)));

        // Create available book
        Book book = Book.builder()
                .title("The Hobbit")
                .author("J.R.R. Tolkien")
                .isbn("9780000000001")
                .publisher("Allen & Unwin")
                .genre(BookGenre.FANTASY)
                .publicationDate(LocalDate.of(1937, 9, 21))
                .availability(true)
                .shelfLocation("A1")
                .build();
        bookId = bookRepository.save(book).getId();
    }

    // Patron borrows a book
    @Test
    void shouldBorrowBook() throws Exception {
        mockMvc.perform(post("/api/v1/borrow-records/")
                        .header("Authorization", patronToken)
                        .param("bookId", bookId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // Librarian gets all borrow records
    @Test
    void shouldGetAllBorrowRecords() throws Exception {
        mockMvc.perform(get("/api/v1/borrow-records/")
                        .header("Authorization", librarianToken))
                .andExpect(status().isOk());
    }

    // Librarian gets borrow records of a specific user
    @Test
    void shouldGetBorrowRecordsByUserId() throws Exception {
        mockMvc.perform(get("/api/v1/borrow-records/user")
                        .param("userId", patronId.toString())
                        .header("Authorization", librarianToken))
                .andExpect(status().isOk());
    }

    // Patron gets own borrow records
    @Test
    void shouldGetBorrowRecordsByCurrentUser() throws Exception {
        mockMvc.perform(get("/api/v1/borrow-records/profile")
                        .header("Authorization", patronToken))
                .andExpect(status().isOk());
    }

    // Patron returns a book
    @Test
    void shouldReturnBorrowedBook() throws Exception {
        // First borrow
        mockMvc.perform(post("/api/v1/borrow-records/")
                        .header("Authorization", patronToken)
                        .param("bookId", bookId.toString()))
                .andExpect(status().isOk());

        // Then return
        Long borrowRecordId = borrowRecordRepository.findAll().get(0).getId();
        mockMvc.perform(patch("/api/v1/borrow-records/return")
                        .param("borrowRecordId", borrowRecordId.toString())
                        .header("Authorization", patronToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // Librarian gets overdue borrow records
    @Test
    void shouldGetOverdueRecords() throws Exception {
        mockMvc.perform(get("/api/v1/borrow-records/overdue")
                        .header("Authorization", librarianToken))
                .andExpect(status().isOk());
    }
}
