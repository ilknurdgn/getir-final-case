package tr.com.getir.getirfinalcase.model.dto.response;

public record GenericResponse<T>(
        Boolean success,
        String message,
        T payload
) {
}
