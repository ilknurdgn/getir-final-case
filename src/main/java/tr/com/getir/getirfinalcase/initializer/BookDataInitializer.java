package tr.com.getir.getirfinalcase.initializer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.enums.BookGenre;
import tr.com.getir.getirfinalcase.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(2)
public class BookDataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (bookRepository.count() == 0) {
            List<Book> books = List.of(
                    Book.builder().title("Suç ve Ceza").author("Fyodor Dostoyevski").isbn("9786053609776")
                            .publisher("Can Yayınları").genre(BookGenre.CLASSIC).publicationDate(LocalDate.of(2015, 3, 15))
                            .availability(true).shelfLocation("A1").build(),
                    Book.builder().title("Küçük Prens").author("Antoine de Saint-Exupéry").isbn("9789750733195")
                            .publisher("Can Çocuk").genre(BookGenre.CHILDREN).publicationDate(LocalDate.of(2020, 5, 10))
                            .availability(true).shelfLocation("A2").build(),
                    Book.builder().title("1984").author("George Orwell").isbn("9786053758962")
                            .publisher("Can Yayınları").genre(BookGenre.FICTION).publicationDate(LocalDate.of(2012, 10, 1))
                            .availability(true).shelfLocation("A3").build(),
                    Book.builder().title("Sefiller").author("Victor Hugo").isbn("9789750718536")
                            .publisher("İş Bankası Kültür Yayınları").genre(BookGenre.CLASSIC).publicationDate(LocalDate.of(2018, 6, 12))
                            .availability(true).shelfLocation("A4").build(),
                    Book.builder().title("Bilinmeyen Bir Kadının Mektubu").author("Stefan Zweig").isbn("9786052950763")
                            .publisher("İş Bankası Kültür Yayınları").genre(BookGenre.ROMANCE).publicationDate(LocalDate.of(2019, 11, 20))
                            .availability(true).shelfLocation("A5").build()
            );
            bookRepository.saveAll(books);
        }
    }
}
