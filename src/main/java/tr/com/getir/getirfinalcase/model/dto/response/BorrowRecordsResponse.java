package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BorrowRecordsResponse(
        Long id,
        String title,
        String isbn,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate
) {

}
