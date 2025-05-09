package tr.com.getir.getirfinalcase.mapper;

import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;

@Component
public class BookMapper {

    public Book mapBookCreateRequestToBook(BookCreateRequest request){
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .publisher(request.publisher())
                .genre(request.genre())
                .publicationDate(request.publicationDate())
                .stockCount(request.stockCount())
                .availableCount(request.stockCount())
                .build();
    }

    public BookResponse mapBookToBookResponse(Book book) {

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .genre(book.getGenre())
                .publicationDate(book.getPublicationDate())
                .stockCount(book.getStockCount())
                .availableCount(book.getAvailableCount())
                .build();
    }
}
