package tr.com.getir.getirfinalcase.initializer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.enums.UserRole;
import tr.com.getir.getirfinalcase.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class UserDataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() == 0) {
            User librarian = User.builder()
                    .name("John")
                    .surname("Doe ")
                    .email("john@example.com")
                    .password(passwordEncoder.encode("password123"))
                    .userRole(UserRole.LIBRARIAN)
                    .phoneNumber("05550000000")
                    .build();

            User patron1 = User.builder()
                    .name("Jane")
                    .surname("Doe")
                    .email("jane@example.com")
                    .password(passwordEncoder.encode("password456"))
                    .userRole(UserRole.PATRON)
                    .phoneNumber("05551234567")
                    .build();

            User patron2 = User.builder()
                    .name("Richard")
                    .surname("Roe ")
                    .email("richard@example.com")
                    .password(passwordEncoder.encode("password789"))
                    .userRole(UserRole.PATRON)
                    .phoneNumber("05557654321")
                    .build();

            userRepository.saveAll(List.of(librarian, patron1, patron2));
        }
    }
}
