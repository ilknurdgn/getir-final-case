package tr.com.getir.getirfinalcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tr.com.getir.getirfinalcase.exception.errormessages.GeneralErrorMessage;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.dto.response.GenericResponse;
import tr.com.getir.getirfinalcase.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Operations related to book management")
public class BookController {

    private final BookService bookService;

    // ADD BOOK
    @Operation(
            summary = "Add a new book to the library",
            description = "Allows librarians to add a new book to the system. ISBN must be unique. Accessible only by users with LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book added successfully"),
            @ApiResponse(responseCode = "409", description = "Book with this ISBN already exists"
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action."
                    , content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/")
    public GenericResponse<Void> addBook(@RequestBody @Valid BookCreateRequest request) {
        bookService.addBook(request);
        return new GenericResponse<>(true, "Book added successfully", null);
    }

    // GET BOOK BY ID
    @Operation(
            summary = "Get book details by id",
            description = "Retrieves detailed information about a book by its id. Accessible by users with LIBRARIAN or PATRON role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping("/{id}")
    public GenericResponse<BookResponse> getBookById (@PathVariable Long id){
        BookResponse response = bookService.getBookById(id);
        return new GenericResponse<>(true, "Book details retrieved successfully", response);
    }
}
