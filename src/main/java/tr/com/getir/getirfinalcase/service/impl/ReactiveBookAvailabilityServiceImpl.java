package tr.com.getir.getirfinalcase.service.impl;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import tr.com.getir.getirfinalcase.model.event.BookAvailabilityEvent;
import tr.com.getir.getirfinalcase.service.ReactiveBookAvailabilityService;

@Service
public class ReactiveBookAvailabilityServiceImpl implements ReactiveBookAvailabilityService {

    private final Sinks.Many<BookAvailabilityEvent> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void publishAvailabilityChange(BookAvailabilityEvent event) {
        sink.tryEmitNext(event);
    }

    @Override
    public Flux<BookAvailabilityEvent> getAvailabilityStream() {
        return sink.asFlux();
    }
}
