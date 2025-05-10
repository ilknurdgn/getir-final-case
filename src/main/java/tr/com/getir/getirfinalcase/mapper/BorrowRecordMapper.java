package tr.com.getir.getirfinalcase.mapper;

import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.dto.response.BorrowRecordWithUserResponse;
import tr.com.getir.getirfinalcase.model.dto.response.BorrowRecordsResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.entity.BorrowRecord;
import tr.com.getir.getirfinalcase.model.entity.User;

import java.time.LocalDate;

@Component
public class BorrowRecordMapper {

    public BorrowRecord toBorrowRecord(User user, Book book){
        return BorrowRecord.builder()
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(2))
                .user(user)
                .book(book)
                .build();
    }

    public BorrowRecordsResponse toBorrowRecordResponse(BorrowRecord borrowRecord){
        return BorrowRecordsResponse.builder()
                .id(borrowRecord.getId())
                .borrowDate(borrowRecord.getBorrowDate())
                .dueDate(borrowRecord.getDueDate())
                .returnDate(borrowRecord.getReturnDate())
                .book(new BorrowRecordsResponse.BookInfo(
                        borrowRecord.getBook().getId(),
                        borrowRecord.getBook().getTitle(),
                        borrowRecord.getBook().getAuthor(),
                        borrowRecord.getBook().getIsbn()
                ))
                .build();
    }

    public BorrowRecordWithUserResponse toWithUserResponse(BorrowRecord borrowRecord){
        return BorrowRecordWithUserResponse.builder()
                .id(borrowRecord.getId())
                .borrowDate(borrowRecord.getBorrowDate())
                .dueDate(borrowRecord.getDueDate())
                .returnDate(borrowRecord.getReturnDate())
                .book(new BorrowRecordWithUserResponse.BookInfo(
                        borrowRecord.getBook().getId(),
                        borrowRecord.getBook().getTitle(),
                        borrowRecord.getBook().getAuthor(),
                        borrowRecord.getBook().getIsbn()
                ))
                .user(new BorrowRecordWithUserResponse.UserInfo(
                        borrowRecord.getUser().getId(),
                        borrowRecord.getUser().getName(),
                        borrowRecord.getUser().getSurname(),
                        borrowRecord.getUser().getEmail()
                ))
                .build();
    }
}

