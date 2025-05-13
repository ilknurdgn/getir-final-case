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
        Book book = borrowRecord.getBook();

        return BorrowRecordsResponse.builder()
                .id(borrowRecord.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .borrowDate(borrowRecord.getBorrowDate())
                .dueDate(borrowRecord.getDueDate())
                .returnDate(borrowRecord.getReturnDate())
                .build();
    }

    public BorrowRecordWithUserResponse toWithUserResponse(BorrowRecord borrowRecord){
        User user = borrowRecord.getUser();
        Book book = borrowRecord.getBook();

        return BorrowRecordWithUserResponse.builder()
                .id(borrowRecord.getId())
                .userFullName(user.getName() + " " + user.getSurname())
                .userEmail(user.getEmail())
                .bookTitle(book.getTitle())
                .isbn(book.getIsbn())
                .borrowDate(borrowRecord.getBorrowDate())
                .dueDate(borrowRecord.getDueDate())
                .returnDate(borrowRecord.getReturnDate())
                .build();
    }
}

