package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;

public interface BookService {
    void addBook(BookCreateRequest request);
}
