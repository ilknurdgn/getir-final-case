package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.request.UserUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUser(Long id);

    List<UserResponse> getAllUsers();

    void updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
