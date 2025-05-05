package tr.com.getir.getirfinalcase.exception.errormessages;


import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record GeneralErrorMessage(
        @Schema(example = "false")
        boolean success,
        String message,
        LocalDateTime localDateTime) {
}
