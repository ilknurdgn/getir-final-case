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
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.repository.BorrowRecordRepository;
import tr.com.getir.getirfinalcase.repository.UserRepository;
import tr.com.getir.getirfinalcase.service.BorrowRecordService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final BorrowRecordMapper borrowRecordMapper;
    private final UserRepository userRepository;

    // BORROW BOOK
    @Override
    @Transactional
    public void borrowBook(User user, Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if(!book.getAvailability()){
            throw new BookNotAvailableException("Book is currently not available for borrowing");
        }

        boolean hasOverdue = borrowRecordRepository
                .existsByUserAndDueDateBeforeAndReturnDateIsNull(
                        user, LocalDate.now()
                );

        if(hasOverdue){
            throw new UserHasOverdueRecordException("The user has an overdue borrowing record and cannot borrow a new book");
        }

        book.setAvailability(false);
        bookRepository.save(book);
        BorrowRecord borrowRecord = borrowRecordMapper.toBorrowRecord(user, book);
        borrowRecordRepository.save(borrowRecord);
    }

    // GET BORROW RECORDS BY USER ID
    @Override
    public List<BorrowRecordsResponse> getBorrowRecordsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found");
        }

        List<BorrowRecord> borrowRecordList = borrowRecordRepository.findByUserId(userId);

        return borrowRecordList.stream()
                .map(borrowRecordMapper::toBorrowRecordResponse)
                .toList();

    }

    // GET ALL BORROW RECORDS
    @Override
    public List<BorrowRecordWithUserResponse> getAllBorrowRecords() {
        List<BorrowRecord> borrowRecordList = borrowRecordRepository.findAll();

        return borrowRecordList.stream()
                .map(borrowRecordMapper::toWithUserResponse)
                .toList();
    }

    // RETURN BOOK
    @Override
    @Transactional
    public void returnBook(Long borrowRecordId, Long userId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowRecordId)
                .orElseThrow(() -> new EntityNotFoundException("Borrow record not found"));

        if(!borrowRecord.getUser().getId().equals(userId)){
            throw new UnauthorizedBorrowReturnException("Returning a book borrowed by another user is not permitted");
        }

        if (borrowRecord.getReturnDate() != null) {
            throw new IllegalStateException("This book was already returned");
        }

        borrowRecord.setReturnDate(LocalDate.now());

        Book book = borrowRecord.getBook();
        book.setAvailability(true);

        bookRepository.save(book);
        borrowRecordRepository.save(borrowRecord);
    }

    // GET OVERDUE RECORDS
    @Override
    @Transactional
    public List<BorrowRecordWithUserResponse> getOverdueRecords() {
        List<BorrowRecord> overdueRecords = borrowRecordRepository.findByReturnDateIsNullAndDueDateBefore(LocalDate.now());

        return overdueRecords.stream()
                .map(borrowRecordMapper::toWithUserResponse)
                .toList();
    }
}
