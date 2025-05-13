package tr.com.getir.getirfinalcase.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import tr.com.getir.getirfinalcase.exception.EmailAlreadyExistException;
import tr.com.getir.getirfinalcase.exception.InvalidCredentialsException;
import tr.com.getir.getirfinalcase.mapper.UserMapper;
import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.UserLoginRequest;
import tr.com.getir.getirfinalcase.model.dto.response.AuthenticationResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.security.CustomUserDetails;
import tr.com.getir.getirfinalcase.security.CustomUserDetailsService;
import tr.com.getir.getirfinalcase.security.JwtUtil;
import tr.com.getir.getirfinalcase.service.AuthenticationService;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthenticationResponse register(UserCreateRequest request) {
        checkIfEmailExists(request.email());
        User user = userMapper.toUser(request);
        userRepository.save(user);
        String token = generateToken(user.getEmail());
        return new AuthenticationResponse(token);
    }


    @Override
    public AuthenticationResponse login(UserLoginRequest request) {
        authenticate(request);
        String token = generateToken(request.email());
        return new AuthenticationResponse(token);
    }


    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }


    private void checkIfEmailExists(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistException("Email already exist");
        }
    }


    private String generateToken(String email) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }


    private void authenticate(UserLoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.password())
            );
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Incorrect email or password");
        }
    }
}
