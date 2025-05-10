package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BorrowRecordsResponse(
        Long id,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BookInfo book
) {
    public record BookInfo(Long id, String title, String author, String isbn) {}
}
