package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;
import tr.com.getir.getirfinalcase.model.enums.UserRole;

@Builder
public record UserResponse(
        Long id,
        String name,
        String surname,
        String email,
        String phoneNumber,
        UserRole userRole
) {
}
