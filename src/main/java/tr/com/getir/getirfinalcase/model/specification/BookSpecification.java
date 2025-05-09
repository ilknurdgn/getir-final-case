package tr.com.getir.getirfinalcase.model.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import tr.com.getir.getirfinalcase.model.dto.request.BookSearchCriteriaRequest;
import tr.com.getir.getirfinalcase.model.entity.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookSpecification {

    public static Specification<Book> filter(BookSearchCriteriaRequest c) {
        return (root, query, cb) -> {
            // 1) Liste oluştur
            List<Predicate> predicates = new ArrayList<>();

            // 2) Her kriter için varsa ekle
            if (c.title() != null && !c.title().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + c.title().toLowerCase() + "%"
                        )
                );
            }

            if (c.author() != null && !c.author().isBlank()) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("author")),
                                "%" + c.author().toLowerCase() + "%"
                        )
                );
            }

            if (c.isbn() != null && !c.isbn().isBlank()) {
                predicates.add(
                        cb.equal(
                                cb.lower(root.get("isbn")),
                                c.isbn().toLowerCase()
                        )
                );
            }

            if (c.genre() != null) {
                predicates.add(
                        cb.equal(root.get("genre"), c.genre())
                );
            }

            // 3) Tümünü AND ile birleştir ve döndür
            return cb.and(predicates.toArray(new Predicate[0]));
        };

    }
}
