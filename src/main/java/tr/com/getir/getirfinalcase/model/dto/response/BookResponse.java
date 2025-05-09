package tr.com.getir.getirfinalcase.model.dto.response;

import lombok.Builder;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;

import java.time.LocalDate;

@Builder
public record BookResponse(
        Long id,
        String title,
        String author,
        String isbn,
        String publisher,
        BookGenre genre,
        LocalDate publicationDate,
        int stockCount,
        int availableCount
) {
}
