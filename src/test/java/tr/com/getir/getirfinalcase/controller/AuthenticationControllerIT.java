package tr.com.getir.getirfinalcase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.UserLoginRequest;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthenticationControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    /**
     * Setup method to insert a test user into the in-memory H2 database
     * before running authentication tests.
     */
    @BeforeAll
    void setup() {
        var encoder = new BCryptPasswordEncoder();

        User user = User.builder()
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .password(encoder.encode("123456789"))
                .phoneNumber("05445013798")
                .userRole(UserRole.LIBRARIAN)
                .build();

        userRepository.save(user);
    }

    // Valid login credentials should return 200 OK and a JWT token.
    @Test
    void shouldAuthenticateWithValidCredentials() throws Exception {
        var request = new UserLoginRequest("john.doe@example.com", "123456789");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.payload.token").exists());
    }

    // Incorrect password should result in 401 Unauthorized.
    @Test
    void shouldFailAuthenticationWithInvalidPassword() throws Exception {
        var request = new UserLoginRequest("john.doe@example.com", "wrongpass");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false));
    }

    // Missing required login fields should result in 400 Bad Request.
    @Test
    void shouldFailAuthenticationWithMissingFields() throws Exception {
        var request = new UserLoginRequest(null, null);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // Valid registration data should result in 201 Created and a JWT token.
    @Test
    void shouldRegisterWithValidData() throws Exception {
        var request = new UserCreateRequest(
                "John",
                "Doe",
                "john@example.com",
                "123456789",
                UserRole.LIBRARIAN,
                "05445013798"

        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.payload.token").exists());
    }

    // Attempting to register with an existing email should return 409 Conflict.
    @Test
    void shouldFailRegisterWithDuplicateEmail() throws Exception {
        var request = new UserCreateRequest(
                "John",
                "Doe",
                "john@example.com",
                "123456789",
                UserRole.LIBRARIAN,
                "05445013798"
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false));
    }
}

