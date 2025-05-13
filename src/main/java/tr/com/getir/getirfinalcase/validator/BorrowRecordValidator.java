package tr.com.getir.getirfinalcase.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.exception.BookNotAvailableException;
import tr.com.getir.getirfinalcase.exception.UnauthorizedBorrowReturnException;
import tr.com.getir.getirfinalcase.exception.UserHasOverdueRecordException;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.entity.BorrowRecord;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.repository.BorrowRecordRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class BorrowRecordValidator {

    private final BorrowRecordRepository borrowRecordRepository;

    public void validateBookIsAvailable(Book book){
        if(!book.getAvailability()){
            throw new BookNotAvailableException("Book is currently not available for borrowing");
        }
    }


    public void validateUserHasNoOverdueRecords(User user){
        boolean hasOverdue = borrowRecordRepository
                .existsByUserAndDueDateBeforeAndReturnDateIsNull(
                        user, LocalDate.now()
                );

        if(hasOverdue){
            throw new UserHasOverdueRecordException("The user has an overdue borrowing record and cannot borrow a new book");
        }
    }

    public void validateReturnAuthorization(BorrowRecord borrowRecord, Long userId){
        if(!borrowRecord.getUser().getId().equals(userId)){
            throw new UnauthorizedBorrowReturnException("Returning a book borrowed by another user is not permitted");
        }
    }


    public void validateNotAlreadyReturned(BorrowRecord borrowRecord){
        if (borrowRecord.getReturnDate() != null) {
            throw new IllegalStateException("This book was already returned");
        }
    }
}
