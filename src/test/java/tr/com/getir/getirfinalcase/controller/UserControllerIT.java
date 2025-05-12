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
import tr.com.getir.getirfinalcase.security.JwtUtil;
import tr.com.getir.getirfinalcase.security.CustomUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String jwtToken;
    private Long userId;

    /**
     * Setup method to insert a test user into the in-memory H2 database
     * before running authentication tests.
     */
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

        User savedUser = userRepository.save(user);
        userId = savedUser.getId();

        jwtToken = jwtUtil.generateToken(new CustomUserDetails(savedUser));
    }

    //  Should successfully retrieve the current user's profile with a valid JWT token.
    @Test
    void shouldGetUserProfileWithValidToken() throws Exception {
        mockMvc.perform(get("/api/v1/users/profile")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.payload.email").value("john.doe@example.com"));
    }

    // Should update the current user's profile with valid fields.
    @Test
    void shouldUpdateUserProfile() throws Exception {
        var request = new UserUpdateRequest(
                "UpdatedName",
                "UpdatedSurname",
                null,
                null,
                "05551112233"
        );

        mockMvc.perform(patch("/api/v1/users/profile")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // Should delete the authenticated user's profile.
    @Test
    void shouldDeleteUserProfile() throws Exception {
        mockMvc.perform(delete("/api/v1/users/profile")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // Should return 403 Forbidden when trying to access the endpoint without a token.
    @Test
    void shouldFailWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/users/profile"))
                .andExpect(status().isForbidden());
    }
}
