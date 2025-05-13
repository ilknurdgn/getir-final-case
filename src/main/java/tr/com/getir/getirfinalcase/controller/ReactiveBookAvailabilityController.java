package tr.com.getir.getirfinalcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import tr.com.getir.getirfinalcase.model.event.BookAvailabilityEvent;
import tr.com.getir.getirfinalcase.service.ReactiveBookAvailabilityService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/books")
@Tag(name = "Book Availability", description = "Real-time availability updates for books")
public class ReactiveBookAvailabilityController {

    private final ReactiveBookAvailabilityService reactiveBookAvailabilityService;

    @Operation(
            summary = "Stream book availability changes",
            description = "Streams real-time book availability updates using Server-Sent Events (SSE). Accessible by LIBRARIAN and PATRON roles."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stream started successfully",
                    content = @Content(mediaType = "text/event-stream",
                            array = @ArraySchema(schema = @Schema(implementation = BookAvailabilityEvent.class)))),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping(value = "/availability", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<BookAvailabilityEvent> streamAvailability() {
        return reactiveBookAvailabilityService.getAvailabilityStream();
    }
}
