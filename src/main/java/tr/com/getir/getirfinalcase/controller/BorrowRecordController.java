package tr.com.getir.getirfinalcase.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tr.com.getir.getirfinalcase.model.dto.response.GenericResponse;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.service.AuthenticationService;
import tr.com.getir.getirfinalcase.service.BorrowRecordService;

@RestController
@RequestMapping("/api/v1/borrow-records")
@RequiredArgsConstructor
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;
    private final AuthenticationService authenticationService;

    // BORROW BOOK
    @PreAuthorize("hasRole('PATRON')")
    @PostMapping("/borrow")
    public GenericResponse<Void> borrowBook(@RequestParam Long bookId){
        User user = authenticationService.getAuthenticatedUser();
        borrowRecordService.borrowBook(user, bookId);
        return new GenericResponse<>(true, "Borrowing record created successfully", null);
    }
}
