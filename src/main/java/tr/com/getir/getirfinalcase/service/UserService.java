package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.UserLoginRequest;
import tr.com.getir.getirfinalcase.model.dto.response.AuthResponse;

public interface UserService {
    AuthResponse register(UserCreateRequest request);

    AuthResponse login(UserLoginRequest request);
}
