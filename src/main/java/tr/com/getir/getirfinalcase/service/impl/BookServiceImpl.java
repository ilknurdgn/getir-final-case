package tr.com.getir.getirfinalcase.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.mapper.BookMapper;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookSearchCriteriaRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.dto.response.PagedResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.specification.BookSpecification;
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.service.BookService;
import org.springframework.data.domain.Pageable;
import tr.com.getir.getirfinalcase.validator.BookValidator;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookValidator bookValidator;


    @Override
    public void addBook(BookCreateRequest request) {
        bookValidator.validateIsbnUnique(request.isbn());
        Book book = bookMapper.mapBookCreateRequestToBook(request);
        bookRepository.save(book);
    }


    @Override
    public BookResponse getBookById(Long id) {
        Book book = getBook(id);
        return bookMapper.mapBookToBookResponse(book);
    }


    @Override
    public PagedResponse<BookResponse> getAllBooks(Pageable pageable) {
        Page<BookResponse> page = bookRepository.findAll(pageable)
                .map(bookMapper::mapBookToBookResponse);

        return PagedResponse.of(page);
    }



    @Override
    public PagedResponse<BookResponse> searchBooks(BookSearchCriteriaRequest criteria, Pageable pageable) {
        Specification<Book> specification = BookSpecification.filter(criteria);
        Page<Book> booksPage = bookRepository.findAll(specification, pageable);
        Page<BookResponse> mappedPage = booksPage.map(bookMapper::mapBookToBookResponse);

        return PagedResponse.of(mappedPage);
    }


    @Override
    public void updateBook(Long id, BookUpdateRequest request) {
        Book book = getBook(id);

        Optional.ofNullable(request.isbn())
                .filter(newIsbn -> !newIsbn.equals(book.getIsbn()))
                .ifPresent(newIsbn -> {
                    bookValidator.validateIsbnUnique(newIsbn);
                    book.setIsbn(newIsbn);
                });

        bookMapper.updateBookFromRequest(request, book);
        bookRepository.save(book);
    }


    @Override
    public void deleteBook(Long id) {
        Book book = getBook(id);
        bookRepository.delete(book);
    }


    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

}
