package tr.com.getir.getirfinalcase.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.getir.getirfinalcase.exception.BookNotAvailableException;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.exception.UnauthorizedBorrowReturnException;
import tr.com.getir.getirfinalcase.exception.UserHasOverdueRecordException;
import tr.com.getir.getirfinalcase.mapper.BorrowRecordMapper;
import tr.com.getir.getirfinalcase.model.dto.response.BorrowRecordWithUserResponse;
import tr.com.getir.getirfinalcase.model.dto.response.BorrowRecordsResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.entity.BorrowRecord;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.model.event.BookAvailabilityEvent;
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.repository.BorrowRecordRepository;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.service.BorrowRecordService;
import tr.com.getir.getirfinalcase.service.ReactiveBookAvailabilityService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordMapper borrowRecordMapper;
    private final UserRepository userRepository;
    private final ReactiveBookAvailabilityService reactiveBookAvailabilityService;


    @Override
    @Transactional
    public void borrowBook(User user, Long bookId) {
        Book book = getBook(bookId);
        validateBookIsAvailable(book);
        validateUserHasNoOverdueRecords(user);

        book.setAvailability(false);
        bookRepository.save(book);

        BorrowRecord borrowRecord = borrowRecordMapper.toBorrowRecord(user, book);
        borrowRecordRepository.save(borrowRecord);

        publishAvailabilityEvent(bookId, false);
    }


    @Override
    public List<BorrowRecordsResponse> getBorrowRecordsByUserId(Long userId) {
        validateUserExists(userId);

        return  borrowRecordRepository.findByUserId(userId)
                .stream()
                .map(borrowRecordMapper::toBorrowRecordResponse)
                .toList();
    }


    @Override
    public List<BorrowRecordWithUserResponse> getAllBorrowRecords() {
        return borrowRecordRepository.findAll()
                .stream()
                .map(borrowRecordMapper::toWithUserResponse)
                .toList();
    }


    @Override
    @Transactional
    public void returnBook(Long borrowRecordId, Long userId) {
        BorrowRecord borrowRecord = getBorrowRecord(borrowRecordId);
        validateReturnAuthorization(borrowRecord, userId);

        validateNotAlreadyReturned(borrowRecord);

        borrowRecord.setReturnDate(LocalDate.now());

        Book book = borrowRecord.getBook();
        book.setAvailability(true);

        bookRepository.save(book);
        borrowRecordRepository.save(borrowRecord);

        publishAvailabilityEvent(book.getId(), true);
    }


    @Override
    @Transactional
    public List<BorrowRecordWithUserResponse> getOverdueRecords() {

        return borrowRecordRepository.findByReturnDateIsNullAndDueDateBefore(LocalDate.now())
                .stream()
                .map(borrowRecordMapper::toWithUserResponse)
                .toList();
    }


    private Book getBook(Long bookId){
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }


    private void validateBookIsAvailable(Book book){
        if(!book.getAvailability()){
            throw new BookNotAvailableException("Book is currently not available for borrowing");
        }
    }


    private void validateUserHasNoOverdueRecords(User user){
        boolean hasOverdue = borrowRecordRepository
                .existsByUserAndDueDateBeforeAndReturnDateIsNull(
                        user, LocalDate.now()
                );

        if(hasOverdue){
            throw new UserHasOverdueRecordException("The user has an overdue borrowing record and cannot borrow a new book");
        }
    }


    private void publishAvailabilityEvent(Long bookId, boolean availability){
        reactiveBookAvailabilityService.publishAvailabilityChange(
                new BookAvailabilityEvent(bookId, availability)
        );
    }

    private void validateUserExists(Long userId){
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }
    }


    private BorrowRecord getBorrowRecord(Long id){
        return borrowRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));
    }


    private void validateReturnAuthorization(BorrowRecord borrowRecord, Long userId){
        if(!borrowRecord.getUser().getId().equals(userId)){
            throw new UnauthorizedBorrowReturnException("Returning a book borrowed by another user is not permitted");
        }
    }


    private void validateNotAlreadyReturned(BorrowRecord borrowRecord){
        if (borrowRecord.getReturnDate() != null) {
            throw new IllegalStateException("This book was already returned");
        }
    }
}
