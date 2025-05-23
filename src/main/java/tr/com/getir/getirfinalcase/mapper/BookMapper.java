package tr.com.getir.getirfinalcase.mapper;

import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
import tr.com.getir.getirfinalcase.model.dto.request.BookUpdateRequest;
import tr.com.getir.getirfinalcase.model.dto.response.BookResponse;
import tr.com.getir.getirfinalcase.model.entity.Book;

import java.util.Optional;

@Component
public class BookMapper {

    public Book toBook(BookCreateRequest request){
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .publisher(request.publisher())
                .genre(request.genre())
                .publicationDate(request.publicationDate())
                .availability(true)
                .shelfLocation(request.shelfLocation())
                .build();
    }

    public BookResponse toBookResponse(Book book) {

        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .publisher(book.getPublisher())
                .genre(book.getGenre())
                .publicationDate(book.getPublicationDate())
                .availability(book.getAvailability())
                .shelfLocation(book.getShelfLocation())
                .build();
    }

    public void updateBookFromRequest(BookUpdateRequest request, Book book){
        Optional.ofNullable(request.title()).ifPresent(book::setTitle);
        Optional.ofNullable(request.author()).ifPresent(book::setAuthor);
        Optional.ofNullable(request.publisher()).ifPresent(book::setPublisher);
        Optional.ofNullable(request.genre()).ifPresent(book::setGenre);
        Optional.ofNullable(request.publicationDate()).ifPresent(book::setPublicationDate);
        Optional.ofNullable(request.shelfLocation()).ifPresent(book::setShelfLocation);


    }
}
