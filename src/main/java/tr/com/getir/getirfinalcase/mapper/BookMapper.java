package tr.com.getir.getirfinalcase.mapper;

import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.dto.request.BookCreateRequest;
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
}
