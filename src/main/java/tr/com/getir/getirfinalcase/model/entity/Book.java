package tr.com.getir.getirfinalcase.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tr.com.getir.getirfinalcase.model.entity.common.Auditable;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "books")
public class Book extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "isbn", unique = true, nullable = false)
    private String isbn;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "genre", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    @Column(name = "publication_date", nullable = false)
    private LocalDate publicationDate;

    @Min(value = 1, message = "At least one copy must be created")
    @Column(name = "stock_count", nullable = false)
    private int stockCount;

    @Column(name = "available_count")
    private int availableCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return id != null && id.equals(book.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}


