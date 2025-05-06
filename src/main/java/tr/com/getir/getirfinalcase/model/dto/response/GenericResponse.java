package tr.com.getir.getirfinalcase.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GenericResponse<T>(
        Boolean success,
        String message,
        T payload
) {
}
