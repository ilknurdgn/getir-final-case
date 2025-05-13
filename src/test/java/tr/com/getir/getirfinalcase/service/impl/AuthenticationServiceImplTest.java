package tr.com.getir.getirfinalcase.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import tr.com.getir.getirfinalcase.exception.EmailAlreadyExistException;
import tr.com.getir.getirfinalcase.exception.InvalidCredentialsException;
import tr.com.getir.getirfinalcase.mapper.UserMapper;
import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.UserLoginRequest;
import tr.com.getir.getirfinalcase.model.dto.response.AuthenticationResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.security.CustomUserDetails;
import tr.com.getir.getirfinalcase.security.CustomUserDetailsService;
import tr.com.getir.getirfinalcase.security.JwtUtil;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // GIVEN
        UserCreateRequest request = createMockUserRequest();
        User user = createMockUser();

        // Mock the calls
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(userMapper.toUser(request)).thenReturn(user);
        when(jwtUtil.generateToken(any())).thenReturn("mocked-token");

        // WHEN
        AuthenticationResponse response = authenticationService.register(request);

        // THEN
        assertNotNull(response);
        assertEquals("mocked-token", response.token());
        verify(userRepository).save(user);

    }

    // REGISTER - Failure
    @Test
    void shouldThrowEmailAlreadyExistException_whenEmailAlreadyExists() {
        // GIVEN
        UserCreateRequest request = createMockUserRequest();

        // Mock the calls
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        // WHEN & THEN
        assertThrows(EmailAlreadyExistException.class, () -> authenticationService.register(request));
    }

    // LOGIN - Success
    @Test
    void shouldReturnToken_whenLoginIsSuccessful() {
        // WHEN & THEN
        UserLoginRequest request = createMockLoginRequest();
        User user = createMockUser();

        // Mock the calls
        when(customUserDetailsService.loadUserByUsername(request.email())).thenReturn(new CustomUserDetails(user));
        when(jwtUtil.generateToken(any())).thenReturn("login-token");

        // WHEN
        AuthenticationResponse response = authenticationService.login(request);

        // THEN
        assertNotNull(response);
        assertEquals("login-token", response.token());
    }


    // Login - Failure
    @Test
    void shouldThrowInvalidCredentialsException_whenCredentialsAreWrong() {
        // GIVEN
        UserLoginRequest request = createMockLoginRequest();

        doThrow(new BadCredentialsException("invalid")).when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // WHEN & THEN
        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(request));
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

    private UserLoginRequest createMockLoginRequest() {
        return new UserLoginRequest("ilknur@example.com", "123456789");
    }

}
