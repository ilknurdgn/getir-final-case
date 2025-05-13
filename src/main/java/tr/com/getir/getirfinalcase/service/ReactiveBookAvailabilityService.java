package tr.com.getir.getirfinalcase.service;

import reactor.core.publisher.Flux;
import tr.com.getir.getirfinalcase.model.event.BookAvailabilityEvent;

public interface ReactiveBookAvailabilityService {
    void publishAvailabilityChange(BookAvailabilityEvent event);
    Flux<BookAvailabilityEvent> getAvailabilityStream();
}
