package tr.com.getir.getirfinalcase.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.getir.getirfinalcase.model.enums.UserRole;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(name = "name", length = 80, nullable = false)
    @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Column(name = "surname", length = 80, nullable = false)
    @Size(min = 2, max = 80, message = "Surname must be between 2 and 80 characters")
    private String surname;

    @NotBlank(message = "Email is required")
    @Column(name = "email", length = 100, nullable = false, unique = true)
    @Email(message = "Email is not valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password", length = 100, nullable = false)
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters" )
    private String password;

    @NotBlank(message = "Phone number is required")
    @Column(name = "phone_number", length = 11, nullable = false)
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone number must be valid")
    private String phoneNumber;

    @NotNull(message = "Role is required")
    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
