package tr.com.getir.getirfinalcase.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.exception.DuplicateIsbnException;
import tr.com.getir.getirfinalcase.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class BookValidator {

    private final BookRepository bookRepository;

    public void validateIsbnUnique(String isbn) {
        if (bookRepository.existsByIsbn(isbn)) {
            throw new DuplicateIsbnException("A book with this ISBN already exists");
        }
    }

}
