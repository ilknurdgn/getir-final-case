package tr.com.getir.getirfinalcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tr.com.getir.getirfinalcase.exception.errormessages.GeneralErrorMessage;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookSearchCriteriaRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.dto.response.GenericResponse;
import tr.com.getir.getirfinalcase.model.dto.response.PagedResponse;
import tr.com.getir.getirfinalcase.service.BookService;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Operations related to book management")
public class BookController {

    private final BookService bookService;


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
    @PostMapping
    public GenericResponse<Void> addBook(@RequestBody @Valid BookCreateRequest request) {
        bookService.addBook(request);
        return new GenericResponse<>(true, "Book added successfully", null);
    }


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


    @Operation(summary = "Get all books", description = "Returns a paginated list of all books. Accessible by librarians and patrons.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping
    public GenericResponse<PagedResponse<BookResponse>> getAllBooks(@ParameterObject @PageableDefault(page = 0, size = 5) Pageable pageable){
        PagedResponse<BookResponse> response = bookService.getAllBooks(pageable);
        return new GenericResponse<>(true, "Books retrieved successfully", response);
    }


    @Operation(summary = "Search books", description = "Search for books by title, author, ISBN or genre. Supports pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Books filtered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid search parameters",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PreAuthorize("hasAnyRole('LIBRARIAN', 'PATRON')")
    @GetMapping("/search")
    public GenericResponse<PagedResponse<BookResponse>> searchBooks(
            @ParameterObject BookSearchCriteriaRequest criteria,
            @ParameterObject @PageableDefault(page = 0, size = 5) Pageable pageable){
        PagedResponse<BookResponse> response = bookService.searchBooks(criteria, pageable);
        return new GenericResponse<>(true, "Books filtered successfully", response);
    }


    @Operation(summary = "Partial update book", description = "Updates only provided fields of a book. Accessible only by users with LIBRARIAN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book patched successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PatchMapping("/{id}")
    public GenericResponse<Void> updateBook(@PathVariable Long id, @RequestBody @Valid BookUpdateRequest request){
        bookService.updateBook(id, request);
        return new GenericResponse<>(true, "Book updated successfully", null);
    }


    @Operation(
            summary = "Delete book",
            description = "Deletes an existing book by its ID. Accessible only by users with LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "Book not found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PreAuthorize("hasRole('LIBRARIAN')")
    @DeleteMapping("/{id}")
    public GenericResponse<Void> deleteBook(@PathVariable Long id){
        bookService.deleteBook(id);
        return new GenericResponse<>(true, "Book deleted successfully", null);
    }
}
