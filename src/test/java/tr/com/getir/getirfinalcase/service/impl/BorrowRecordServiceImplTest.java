package tr.com.getir.getirfinalcase.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BorrowRecordServiceImplTest {
    @InjectMocks
    private BorrowRecordServiceImpl borrowRecordService;

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BorrowRecordMapper borrowRecordMapper;

    @Mock
    private UserRepository userRepository;

    // BORROW BOOK - Success
    @Test
    void shouldBorrowBook_whenBookAvailableAndNoOverdueRecord() {
        // GIVEN
        User user = createMockUser();
        Book book = createMockBook();
        BorrowRecord record = createMockBorrowRecord(user, book);

        // Mock the calls
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(borrowRecordRepository.existsByUserAndDueDateBeforeAndReturnDateIsNull(eq(user), any())).thenReturn(false);
        when(borrowRecordMapper.toBorrowRecord(user, book)).thenReturn(record);

        // WHEN
        borrowRecordService.borrowBook(user, 1L);

        // THEN
        verify(bookRepository).save(book);
        verify(borrowRecordRepository).save(record);
    }

    // BORROW BOOK - Book not found
    @Test
    void shouldThrowEntityNotFoundException_whenBookNotFound() {
        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(EntityNotFoundException.class, () -> borrowRecordService.borrowBook(createMockUser(), 1L));
    }

    // BORROW BOOK - Book unavailable
    @Test
    void shouldThrowBookNotAvailableException_whenBookIsNotAvailable() {
        // GIVEN
        Book book = createMockBook();
        book.setAvailability(false);

        // Mock the calls
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // WHEN & THEN
        assertThrows(BookNotAvailableException.class, () -> borrowRecordService.borrowBook(createMockUser(), 1L));
    }

    // BORROW BOOK - User has overdue record
    @Test
    void shouldThrowUserHasOverdueRecordException_whenUserHasOverdueRecord() {
        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.of(createMockBook()));
        when(borrowRecordRepository.existsByUserAndDueDateBeforeAndReturnDateIsNull(any(), any())).thenReturn(true);

        // WHEN & THEN
        assertThrows(UserHasOverdueRecordException.class, () -> borrowRecordService.borrowBook(createMockUser(), 1L));
    }

    // GET BORROW RECORDS BY USER ID - Success
    @Test
    void shouldReturnBorrowRecords_whenUserExists() {
        // GIVEN
        User user = createMockUser();
        BorrowRecord record = createMockBorrowRecord(user, createMockBook());
        BorrowRecordsResponse response = BorrowRecordsResponse.builder().id(1L).build();

        // Mock the calls
        when(userRepository.existsById(1L)).thenReturn(true);
        when(borrowRecordRepository.findByUserId(1L)).thenReturn(List.of(record));
        when(borrowRecordMapper.toBorrowRecordResponse(record)).thenReturn(response);

        // WHEN
        List<BorrowRecordsResponse> result = borrowRecordService.getBorrowRecordsByUserId(1L);

        // THEN
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }

    // GET BORROW RECORDS BY USER ID - User not found
    @Test
    void shouldThrowEntityNotFoundException_whenUserNotFound() {
        // GIVEN
        when(userRepository.existsById(1L)).thenReturn(false);

        // WHEN & THEN
        assertThrows(EntityNotFoundException.class, () -> borrowRecordService.getBorrowRecordsByUserId(1L));
    }

    // RETURN BOOK - Success
    @Test
    void shouldReturnBook_whenValidConditions() {
        // GIVEN
        User user = createMockUser();
        Book book = createMockBook();
        BorrowRecord record = createMockBorrowRecord(user, book);

        // Mock the calls
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(record));

        // WHEN
        borrowRecordService.returnBook(1L, 1L);

        // THEN
        verify(bookRepository).save(book);
        verify(borrowRecordRepository).save(record);
    }

    // RETURN BOOK - Record not found
    @Test
    void shouldThrowEntityNotFoundException_whenBorrowRecordNotFound() {
        // GIVEN
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(EntityNotFoundException.class, () -> borrowRecordService.returnBook(1L, 1L));
    }

    // RETURN BOOK - Unauthorized user
    @Test
    void shouldThrowUnauthorizedBorrowReturnException_whenAnotherUserTriesToReturn() {
        // GIVEN
        BorrowRecord record = createMockBorrowRecord(createMockUser(), createMockBook());

        // Mock the calls
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(record));

        // WHEN & THEN
        assertThrows(UnauthorizedBorrowReturnException.class, () -> borrowRecordService.returnBook(1L, 2L));
    }

    // RETURN BOOK - Already returned
    @Test
    void shouldThrowIllegalStateException_whenAlreadyReturned() {
        // GIVEN
        BorrowRecord record = createMockBorrowRecord(createMockUser(), createMockBook());
        record.setReturnDate(LocalDate.now());

        // Mock the calls
        when(borrowRecordRepository.findById(1L)).thenReturn(Optional.of(record));

        // WHEN & THEN
        assertThrows(IllegalStateException.class, () -> borrowRecordService.returnBook(1L, 1L));
    }

    // GET OVERDUE RECORDS
    @Test
    void shouldReturnOverdueRecords() {
        // GIVEN
        BorrowRecord record = createMockBorrowRecord(createMockUser(), createMockBook());
        BorrowRecordWithUserResponse response = BorrowRecordWithUserResponse.builder().id(1L).build();

        // Mock the calls
        when(borrowRecordRepository.findByReturnDateIsNullAndDueDateBefore(any())).thenReturn(List.of(record));
        when(borrowRecordMapper.toWithUserResponse(record)).thenReturn(response);

        // WHEN
        List<BorrowRecordWithUserResponse> result = borrowRecordService.getOverdueRecords();

        // THEN
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
    }

    // MOCK DATA
    private User createMockUser() {
        return User.builder()
                .id(1L)
                .name("İlknur")
                .surname("Doğan")
                .email("ilknur@example.com")
                .build();
    }

    private Book createMockBook() {
        return Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .availability(true)
                .build();
    }

    private BorrowRecord createMockBorrowRecord(User user, Book book) {
        return BorrowRecord.builder()
                .id(1L)
                .user(user)
                .book(book)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusWeeks(2))
                .build();
    }
}