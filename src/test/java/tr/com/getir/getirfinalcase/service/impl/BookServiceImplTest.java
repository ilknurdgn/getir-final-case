package tr.com.getir.getirfinalcase.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.com.getir.getirfinalcase.exception.DuplicateIsbnException;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.mapper.BookMapper;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;
import tr.com.getir.getirfinalcase.repository.BookRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    // ADD BOOK - Success
    @Test
    void shouldAddBook_whenIsbnIsUnique() {
        // GIVEN
        BookCreateRequest request = createMockCreateRequest();
        Book book = createMockBook();

        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(false);
        when(bookMapper.toBook(request)).thenReturn(book);

        // WHEN
        bookService.addBook(request);

        // THEN
        verify(bookRepository).save(book);
    }

    // ADD BOOK - Failure
    @Test
    void shouldThrowDuplicateIsbnException_whenIsbnAlreadyExists() {
        // GIVEN
        BookCreateRequest request = createMockCreateRequest();
        when(bookRepository.existsByIsbn(request.isbn())).thenReturn(true);

        // WHEN & THEN
        assertThrows(DuplicateIsbnException.class, () -> bookService.addBook(request));
    }

    // GET BOOK BY ID - Success
    @Test
    void shouldReturnBook_whenBookExists() {
        // GIVEN
        Book book = createMockBook();
        BookResponse response = createMockBookResponse();

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toBookResponse(book)).thenReturn(response);

        // WHEN
        BookResponse result = bookService.getBookById(1L);

        // THEN
        assertEquals(response, result);
    }

    // GET BOOK BY ID - Failure
    @Test
    void shouldThrowEntityNotFoundException_whenBookNotFound() {
        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(EntityNotFoundException.class, () -> bookService.getBookById(1L));
    }

    // DELETE BOOK - Success
    @Test
    void shouldDeleteBook_whenBookExists() {
        // GIVEN
        Book book = createMockBook();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // WHEN
        bookService.deleteBook(1L);

        // THEN
        verify(bookRepository).delete(book);
    }

    // DELETE BOOK - Failure
    @Test
    void shouldThrowEntityNotFoundException_whenDeletingNonexistentBook() {
        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBook(1L));
    }

    // Mock Data
    private BookCreateRequest createMockCreateRequest() {
        return new BookCreateRequest(
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                "Prentice Hall",
                BookGenre.TECHNOLOGY,
                LocalDate.of(2008, 8, 11),
                "G1"

        );
    }

    private Book createMockBook() {
        return Book.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .publisher("Prentice Hall")
                .genre(BookGenre.TECHNOLOGY)
                .publicationDate(LocalDate.of(2008, 8, 11))
                .availability(true)
                .shelfLocation("G1")
                .build();
    }

    private BookResponse createMockBookResponse() {
        return BookResponse.builder()
                .id(1L)
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .publisher("Prentice Hall")
                .genre(BookGenre.TECHNOLOGY)
                .publicationDate(LocalDate.of(2008, 8, 11))
                .availability(true)
                .shelfLocation("G1")
                .build();
    }
}
