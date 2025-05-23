package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookSearchCriteriaRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import org.springframework.data.domain.Pageable;
import tr.com.getir.getirfinalcase.model.dto.response.PagedResponse;

public interface BookService {
    void addBook(BookCreateRequest request);

    BookResponse getBookById(Long id);

    PagedResponse<BookResponse> getAllBooks(Pageable pageable);

    PagedResponse<BookResponse> searchBooks(BookSearchCriteriaRequest criteria, Pageable pageable);

    void updateBook(Long id, BookUpdateRequest request);

    void deleteBook(Long id);
}
