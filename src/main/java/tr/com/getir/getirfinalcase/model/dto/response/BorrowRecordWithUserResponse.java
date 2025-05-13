package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BorrowRecordWithUserResponse(
        Long id,
        String userFullName,
        String userEmail,
        String bookTitle,
        String isbn,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate
) {

}
