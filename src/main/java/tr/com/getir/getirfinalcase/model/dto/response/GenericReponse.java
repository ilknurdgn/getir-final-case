package tr.com.getir.getirfinalcase.model.dto.response;

public record GenericReponse<T>(
        Boolean success,
        String message,
        T payload
) {
}
