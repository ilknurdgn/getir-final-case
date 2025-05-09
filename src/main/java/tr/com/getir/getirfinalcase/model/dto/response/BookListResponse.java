package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;

@Builder
public record BookListResponse(
        Long id,
        String title,
        String author,
        String publisher,
        BookGenre genre
) {
}
