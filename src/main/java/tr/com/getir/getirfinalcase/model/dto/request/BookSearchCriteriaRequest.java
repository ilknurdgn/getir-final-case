package tr.com.getir.getirfinalcase.model.dto.request;

import lombok.Builder;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;

@Builder
public record BookSearchCriteriaRequest(
        String title,
        String author,
        String isbn,
        BookGenre genre
) {
}
