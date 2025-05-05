package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.UserLoginRequest;
import tr.com.getir.getirfinalcase.model.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(UserCreateRequest request);

    AuthenticationResponse login(UserLoginRequest request);
}
