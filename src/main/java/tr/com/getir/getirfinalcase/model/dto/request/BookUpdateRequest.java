package tr.com.getir.getirfinalcase.model.dto.request;

import tr.com.getir.getirfinalcase.model.enums.BookGenre;

import java.time.LocalDate;

public record BookUpdateRequest(
        String title,
        String author,
        String publisher,
        String isbn,
        BookGenre genre,
        LocalDate publicationDate,
        Integer stockCount

) {
}
