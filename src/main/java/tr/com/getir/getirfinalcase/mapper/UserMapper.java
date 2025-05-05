package tr.com.getir.getirfinalcase.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.entity.User;

@Component
@RequiredArgsConstructor
public class UserMapper{

    private final PasswordEncoder passwordEncoder;

    public User mapUserCreateRequestToUser(UserCreateRequest request) {
        return User.builder()
                .name(request.name())
                .surname(request.surname())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .phoneNumber(request.phoneNumber())
                .userRole(request.userRole())
                .build();
    }
}
