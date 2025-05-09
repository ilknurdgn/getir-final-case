package tr.com.getir.getirfinalcase.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.getir.getirfinalcase.exception.DuplicateIsbnException;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.mapper.BookMapper;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.service.BookService;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    // ADD BOOK
    @Override
    public void addBook(BookCreateRequest request) {

        if(bookRepository.existsByIsbn(request.isbn())){
            throw new DuplicateIsbnException("A book with this ISBN already exists");
        }

        Book book = bookMapper.mapBookCreateRequestToBook(request);
        bookRepository.save(book);
    }

    // GET BOOK BY ID
    @Override
    public BookResponse getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Book not found"));

        return bookMapper.mapBookToBookResponse(book);
    }

}
