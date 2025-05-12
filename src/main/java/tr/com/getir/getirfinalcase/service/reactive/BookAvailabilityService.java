package tr.com.getir.getirfinalcase.service.reactive;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import tr.com.getir.getirfinalcase.model.event.BookAvailabilityEvent;

@Service
public class BookAvailabilityService {

    private final Sinks.Many<BookAvailabilityEvent> sink = Sinks.many().multicast().onBackpressureBuffer();

    public void publishAvailabilityChange(BookAvailabilityEvent event) {
        sink.tryEmitNext(event);
    }

    public Flux<BookAvailabilityEvent> getAvailabilityStream() {
        return sink.asFlux();
    }
}
