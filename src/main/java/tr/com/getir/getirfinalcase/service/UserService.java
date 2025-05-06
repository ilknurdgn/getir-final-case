package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUser(String name);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers();
}
