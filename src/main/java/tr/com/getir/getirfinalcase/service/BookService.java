package tr.com.getir.getirfinalcase.service;

import org.springframework.data.domain.Page;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookListResponse;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import org.springframework.data.domain.Pageable;
import tr.com.getir.getirfinalcase.model.dto.response.PagedResponse;

public interface BookService {
    void addBook(BookCreateRequest request);

    BookResponse getBookById(Long id);

    PagedResponse<BookListResponse> getAllBooks(Pageable pageable);
}
