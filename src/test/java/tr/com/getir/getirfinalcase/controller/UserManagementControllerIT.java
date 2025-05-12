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
import tr.com.getir.getirfinalcase.model.dto.request.UserUpdateRequest;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.security.CustomUserDetails;
import tr.com.getir.getirfinalcase.security.JwtUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserManagementControllerIT {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private JwtUtil jwtUtil;

    private String jwtToken;
    private Long userId;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        var encoder = new BCryptPasswordEncoder();

        User user = User.builder()
                .name("John")
                .surname("Doe")
                .email("john.doe@example.com")
                .password(encoder.encode("123456789"))
                .phoneNumber("05445013798")
                .userRole(UserRole.PATRON)
                .build();

        userRepository.save(user);
        userId = user.getId();

        // Authenticated user with LIBRARIAN role
        User librarian = User.builder()
                .name("Lib")
                .surname("Rarian")
                .email("librarian@example.com")
                .password(encoder.encode("adminpass"))
                .phoneNumber("05001112233")
                .userRole(UserRole.LIBRARIAN)
                .build();

        jwtToken = jwtUtil.generateToken(new CustomUserDetails(userRepository.save(librarian)));
    }

    // Should return the user by ID for a LIBRARIAN
    @Test
    void shouldGetUserById() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.email").value("john.doe@example.com"));
    }

    // Should return all users for a LIBRARIAN
    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users/")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload").isArray());
    }

    // Should update the target user by ID
    @Test
    void shouldUpdateUserById() throws Exception {
        var updateRequest = new UserUpdateRequest(
                "UpdatedName",
                "UpdatedSurname",
                null,
                null,
                "05556667777"
        );

        mockMvc.perform(patch("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }


    // Should delete the target user by ID
    @Test
    void shouldDeleteUserById() throws Exception {
        mockMvc.perform(delete("/api/v1/users/" + userId)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }


    // Should fail when unauthorized (no token)
    @Test
    void shouldFailWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isForbidden());
    }
}
