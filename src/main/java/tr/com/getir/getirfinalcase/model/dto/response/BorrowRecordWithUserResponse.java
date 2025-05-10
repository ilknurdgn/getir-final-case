package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BorrowRecordWithUserResponse(
        Long id,
        LocalDate borrowDate,
        LocalDate dueDate,
        LocalDate returnDate,
        BookInfo book,
        UserInfo user
) {
    public record BookInfo(Long id, String title, String author, String isbn) {}
    public record UserInfo(Long id, String firstName, String lastName, String email) {}
}
