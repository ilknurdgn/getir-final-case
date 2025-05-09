package tr.com.getir.getirfinalcase.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tr.com.getir.getirfinalcase.exception.DuplicateIsbnException;
import tr.com.getir.getirfinalcase.exception.EntityNotFoundException;
import tr.com.getir.getirfinalcase.mapper.BookMapper;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookSearchCriteriaRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookListResponse;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.dto.response.PagedResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.specification.BookSpecification;
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.service.BookService;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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

    // GET ALL BOOKS
    @Override
    public PagedResponse<BookListResponse> getAllBooks(Pageable pageable) {
        Page<BookListResponse> page = bookRepository.findAll(pageable)
                .map(bookMapper::mapBookToBookListResponse);

        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }


    // SEARCH
    @Override
    public PagedResponse<BookListResponse> searchBooks(BookSearchCriteriaRequest criteria, Pageable pageable) {
        Specification<Book> specification = BookSpecification.filter(criteria);
        Page<Book> booksPage = bookRepository.findAll(specification, pageable);

        List<BookListResponse> content = booksPage
                .stream()
                .map(bookMapper::mapBookToBookListResponse)
                .toList();

        return new PagedResponse<>(
                content,
                booksPage.getNumber(),
                booksPage.getSize(),
                booksPage.getTotalElements(),
                booksPage.getTotalPages(),
                booksPage.isLast()
        );
    }

    // UPDATE BOOK
    @Override
    public void updateBook(Long id, BookUpdateRequest request) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Optional.ofNullable(request.title()).ifPresent(book::setTitle);
        Optional.ofNullable(request.author()).ifPresent(book::setAuthor);
        Optional.ofNullable(request.publisher()).ifPresent(book::setPublisher);
        Optional.ofNullable(request.genre()).ifPresent(book::setGenre);
        Optional.ofNullable(request.publicationDate()).ifPresent(book::setPublicationDate);
        Optional.ofNullable(request.stockCount()).ifPresent(book::setStockCount);
        Optional.ofNullable(request.stockCount()).ifPresent(book::setStockCount);

        bookRepository.save(book);
    }


    // DELETE BOOK
    @Override
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        bookRepository.delete(book);
    }


}
