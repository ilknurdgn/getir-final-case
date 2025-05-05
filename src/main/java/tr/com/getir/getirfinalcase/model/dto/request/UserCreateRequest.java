package tr.com.getir.getirfinalcase.model.dto.request;

import jakarta.validation.constraints.*;
import tr.com.getir.getirfinalcase.model.enums.UserRole;

public record UserCreateRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
        String name,

        @NotBlank(message = "Surname is required")
        @Size(min = 2, max = 80, message = "Surname must be between 2 and 80 characters")
        String surname,

        @NotBlank(message = "Email is required")
        @Email(message = "Email is not valid")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters" )
        String password,

        @NotNull(message = "Role is required")
        UserRole userRole,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be valid")
        String phoneNumber
) {
}
