package tr.com.getir.getirfinalcase.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import tr.com.getir.getirfinalcase.exception.EmailAlreadyExistException;
import tr.com.getir.getirfinalcase.mapper.UserMapper;
import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.AuthenticationResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.security.CustomUserDetailsService;
import tr.com.getir.getirfinalcase.security.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private AuthenticationManager authenticationManager;

    // REGISTER - Success
    @Test
    void shouldRegisterUserAndReturnToken_whenEmailIsUnique() {
        UserCreateRequest request = createMockUserRequest();
        User user = createMockUser();

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userMapper.mapUserCreateRequestToUser(request)).thenReturn(user);
        when(jwtUtil.generateToken(any())).thenReturn("mocked-token");

        AuthenticationResponse response = authenticationService.register(request);

        assertNotNull(response);
        assertEquals("mocked-token", response.token());
        verify(userRepository).save(user);

    }

    // REGISTER - Failure
    @Test
    void shouldThrowEmailAlreadyExistException_whenEmailAlreadyExists() {
        UserCreateRequest request = createMockUserRequest();
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistException.class, () -> authenticationService.register(request));
    }

    // MOCK DATA
    private User createMockUser() {
       return User.builder()
               .id(1L)
               .name("İlknur")
               .surname("Doğan")
               .email("ilknur@example.com")
               .password("hashedpassword")
               .phoneNumber("05445013798")
               .userRole(UserRole.PATRON)
               .build();
    }

    private UserCreateRequest createMockUserRequest() {
        return new UserCreateRequest(
                "İlknur",
                "Doğan",
                "ilknur@example.com",
                "123456789",
                UserRole.PATRON,
                "05445013798"
        );
    }


}
