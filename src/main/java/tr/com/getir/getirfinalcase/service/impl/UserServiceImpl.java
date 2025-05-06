package tr.com.getir.getirfinalcase.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.mapper.UserMapper;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // GET USER PROFILE
    @Override
    public UserResponse getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));

        return userMapper.mapToUserResponse(user);
    }

    // GET USER BY ID
    @Override
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return userMapper.mapToUserResponse(user);
    }

    // GET ALL USERS
    @Override
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(userMapper::mapToUserResponse)
                .toList();
    }
}
