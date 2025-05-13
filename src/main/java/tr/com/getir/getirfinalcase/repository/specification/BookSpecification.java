package tr.com.getir.getirfinalcase.repository.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import tr.com.getir.getirfinalcase.model.dto.request.BookSearchCriteriaRequest;
import tr.com.getir.getirfinalcase.model.entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Specification Design Pattern implementation for dynamic book filtering.
 * Used to construct complex query predicates based on optional search criteria.
 */
public class BookSpecification {

    public static Specification<Book> filter(BookSearchCriteriaRequest c) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (isNotBlank(c.title())) {
                predicates.add(containsIgnoreCase(cb, root.get("title"), c.title()));
            }

            if (isNotBlank(c.author())) {
                predicates.add(containsIgnoreCase(cb, root.get("author"), c.author()));
            }

            if (isNotBlank(c.isbn())) {
                predicates.add(cb.equal(cb.lower(root.get("isbn")), c.isbn().toLowerCase()));
            }

            if (c.genre() != null) {
                predicates.add(cb.equal(root.get("genre"), c.genre()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Predicate containsIgnoreCase(CriteriaBuilder cb, Path<String> path, String value) {
        return cb.like(cb.lower(path), "%" + value.toLowerCase() + "%");
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }
}
