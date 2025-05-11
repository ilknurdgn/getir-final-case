package tr.com.getir.getirfinalcase.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.mapper.UserMapper;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    // GET USER - Success
    @Test
    void shouldReturnUserResponse_whenUserExists(){
        // Given
        User user = createMockUser();
        UserResponse expectedResponse = createMockUserResponse();

        // Mock the calls
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.mapToUserResponse(user)).thenReturn(expectedResponse);

        // When
        UserResponse result = userService.getUser(1L);

        // Then
        assertEquals(expectedResponse, result);

    }

    // GET USER - Failure
    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFound(){
        // Mock the calls
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Expect an exception to be thrown
        assertThrows(EntityNotFoundException.class, () -> userService.getUser(1L));
    }

    // GET ALL USERS
    @Test
    void shouldReturnListOfUserResponses_whenUsersExist(){
        // Given
        List<User> users = List.of(createMockUser());
        List<UserResponse> expectedResponses  = List.of(createMockUserResponse());

        // Mock the calls
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.mapToUserResponse(users.getFirst())).thenReturn(expectedResponses.getFirst());

        // When
        List<UserResponse> result = userService.getAllUsers();

        // Then
        assertEquals(expectedResponses.size(), result.size());
        assertEquals(expectedResponses.getFirst(), result.getFirst());
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
}
