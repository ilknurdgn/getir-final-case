package tr.com.getir.getirfinalcase.model.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(

        @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
        String name,

        @Size(min = 2, max = 80, message = "Surname must be between 2 and 80 characters")
        String surname,

        @Email(message = "Email is not valid")
        String email,

        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters" )
        String password,

        @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be valid")
        String phoneNumber
) {
}
