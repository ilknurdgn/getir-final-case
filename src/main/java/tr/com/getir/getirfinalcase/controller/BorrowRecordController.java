package tr.com.getir.getirfinalcase.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tr.com.getir.getirfinalcase.exception.errormessages.GeneralErrorMessage;
import tr.com.getir.getirfinalcase.model.dto.response.BorrowRecordsResponse;
import tr.com.getir.getirfinalcase.model.dto.response.GenericResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.service.AuthenticationService;
import tr.com.getir.getirfinalcase.service.BorrowRecordService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/borrow-records")
@RequiredArgsConstructor
@Tag(name = "Borrow Records", description = "Operations related to borrowing books")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;
    private final AuthenticationService authenticationService;

    // BORROW BOOK
    @Operation(
            summary = "Borrow a book",
            description = "Authenticated patrons can borrow an available book by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Borrowing record created successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "409", description = "Book is currently not available for borrowing or user has an overdue borrowing record and cannot borrow a new book", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
    })

    @PreAuthorize("hasRole('PATRON')")
    @PostMapping("/")
    public GenericResponse<Void> borrowBook(@RequestParam Long bookId){
        User user = authenticationService.getAuthenticatedUser();
        borrowRecordService.borrowBook(user, bookId);
        return new GenericResponse<>(true, "Borrowing record created successfully", null);
    }

    // GET BORROW RECORDS BY USER ID
    @Operation(
            summary = "Get borrow records by user id",
            description = "Retrieves a list of borrow records for the specified user. Accessible only by users with LIBRARIAN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Borrow records successfully retrieved."),
            @ApiResponse(responseCode = "403", description = "Access denied. You are not authorized for this action",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class))),
            @ApiResponse(responseCode = "404", description = "User not found.",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = GeneralErrorMessage.class)))
    })

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping("/")
    public GenericResponse<List<BorrowRecordsResponse>> getBorrowRecordsByUserId(@RequestParam Long userId){
        List<BorrowRecordsResponse>  borrowRecordsResponseList = borrowRecordService.getBorrowRecordsByUserId(userId);
        return new GenericResponse<>(true, "Borrow records successfully retrieved.", borrowRecordsResponseList);
    }
}
