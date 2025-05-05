package tr.com.getir.getirfinalcase.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tr.com.getir.getirfinalcase.exception.EmailAlreadyExistException;
import tr.com.getir.getirfinalcase.mapper.UserMapper;
import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.AuthResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.security.CustomUserDetails;
import tr.com.getir.getirfinalcase.security.JwtUtil;
import tr.com.getir.getirfinalcase.service.UserService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse register(UserCreateRequest request) {

        Optional<User> userOptional = userRepository.findByEmail(request.email());

        if(userOptional.isPresent()){
            throw new EmailAlreadyExistException("Email already exist");
        }

        User user = userMapper.mapUserCreateRequestToUser(request);
        userRepository.save(user);

        return new AuthResponse(jwtUtil.generateToken(new CustomUserDetails(user)));
    }
}
