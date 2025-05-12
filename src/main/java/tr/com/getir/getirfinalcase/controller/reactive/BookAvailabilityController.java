package tr.com.getir.getirfinalcase.controller.reactive;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tr.com.getir.getirfinalcase.model.event.BookAvailabilityEvent;
import tr.com.getir.getirfinalcase.service.reactive.BookAvailabilityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
public class BookAvailabilityController {

    private final BookAvailabilityService bookAvailabilityService;

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping(value = "/availability", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookAvailabilityEvent> streamAvailability() {
        return bookAvailabilityService.getAvailabilityStream();
    }
}
