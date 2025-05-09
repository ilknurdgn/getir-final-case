package tr.com.getir.getirfinalcase.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;

import java.time.LocalDate;

public record BookCreateRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotBlank(message = "ISBN is required")
        String isbn,

        @NotBlank(message = "Publisher is required")
        String publisher,

        @NotNull(message = "Genre must be selected")
        BookGenre genre,

        @NotNull(message = "Publication date is required")
        LocalDate publicationDate,

        @Min(value = 1, message = "At least one copy must be created")
        int stockCount
) {
}
