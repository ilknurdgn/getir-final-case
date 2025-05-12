package tr.com.getir.getirfinalcase.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.dto.request.UserCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.UserUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.UserResponse;
import tr.com.getir.getirfinalcase.model.entity.User;

import java.util.Optional;

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

    public UserResponse mapToUserResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .userRole(user.getUserRole())
                .build();
    }

    public void updateUserFromRequest(UserUpdateRequest request, User user){
        Optional.ofNullable(request.name()).ifPresent(user::setName);
        Optional.ofNullable(request.surname()).ifPresent(user::setSurname);
        Optional.ofNullable(request.email()).ifPresent(user::setEmail);
        Optional.ofNullable(request.phoneNumber()).ifPresent(user::setPhoneNumber);
    }
}
