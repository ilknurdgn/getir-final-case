package tr.com.getir.getirfinalcase.model.event;

public record BookAvailabilityEvent(Long bookId, boolean isAvailable) {
}
