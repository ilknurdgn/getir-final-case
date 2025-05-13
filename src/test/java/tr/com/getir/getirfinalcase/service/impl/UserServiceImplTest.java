package tr.com.getir.getirfinalcase.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.mapper.UserMapper;
import tr.com.getir.getirfinalcase.model.dto.request.UserUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    // GET USER - Success
    @Test
    void shouldReturnUserResponse_whenUserExists(){
        // Given
        User user = createMockUser();
        UserResponse expectedResponse = createMockUserResponse();

        // Mock the calls
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(user)).thenReturn(expectedResponse);

        // When
        UserResponse result = userService.getUserById(1L);

        // Then
        assertEquals(expectedResponse, result);

    }

    // GET USER - Failure
    @Test
    void shouldThrowEntityNotFoundException_whenGettingNonexistentUser(){
        // Mock the calls
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Expect an exception to be thrown
        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(1L));
    }

    // GET ALL USERS
    @Test
    void shouldReturnListOfUserResponses_whenUsersExist(){
        // Given
        List<User> users = List.of(createMockUser());
        List<UserResponse> expectedResponses  = List.of(createMockUserResponse());

        // Mock the calls
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toUserResponse(users.getFirst())).thenReturn(expectedResponses.getFirst());

        // When
        List<UserResponse> result = userService.getAllUsers();

        // Then
        assertEquals(expectedResponses.size(), result.size());
        assertEquals(expectedResponses.getFirst(), result.getFirst());
    }

    // UPDATE USER - Success
    @Test
    void shouldUpdateUser_whenAllFieldsArePresent(){

        // Given
        User user = createMockUser();
        UserUpdateRequest request = createMockUpdateRequest();

        // Mock the calls
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");

        // When
        userService.updateUser(1L, request);

        // Then
        assertEquals("newName", user.getName());
        assertEquals("newSurname", user.getSurname());
        assertEquals("new@example.com", user.getEmail());
        assertEquals("encodedPass", user.getPassword());
        assertEquals("05559998877", user.getPhoneNumber());

        verify(userRepository).save(user);
    }

    // UPDATE USER - Failure
    @Test
        void shouldThrowEntityNotFoundException_whenUpdatingNonexistentUser(){
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, ()-> userService.updateUser(1L, createMockUpdateRequest()));
    }

    // DELETE USER - Success
    @Test
    void shouldDeleteUser_whenUserExists() {
        // GIVEN
        User user = createMockUser();

        // Mock the calls
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // WHEN
        userService.deleteUser(1L);

        // THEN
        verify(userRepository).delete(user);
    }

    // DELETE USER - Failure
    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonexistentUser() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(1L));
    }


    // MOCK DATA
    private User createMockUser(){
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

    private UserResponse createMockUserResponse(){
        return UserResponse.builder()
                .id(1L)
                .name("İlknur")
                .surname("Doğan")
                .email("ilknur@example.com")
                .phoneNumber("05445013798")
                .userRole(UserRole.PATRON)
                .build();

    }

    private UserUpdateRequest createMockUpdateRequest() {
        return UserUpdateRequest.builder()
                .name("newName")
                .surname("newSurname")
                .email("new@example.com")
                .password("newPass")
                .phoneNumber("05559998877")
                .build();
    }
}
