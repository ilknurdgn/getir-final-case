package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;

public interface BookService {
    void addBook(BookCreateRequest request);

    BookResponse getBookById(Long id);
}
