package tr.com.getir.getirfinalcase.mapper;

import org.springframework.stereotype.Component;
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
}

