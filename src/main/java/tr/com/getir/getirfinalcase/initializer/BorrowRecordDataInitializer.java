package tr.com.getir.getirfinalcase.initializer;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tr.com.getir.getirfinalcase.model.entity.Book;
import tr.com.getir.getirfinalcase.model.entity.BorrowRecord;
import tr.com.getir.getirfinalcase.model.entity.User;
import tr.com.getir.getirfinalcase.repository.BookRepository;
import tr.com.getir.getirfinalcase.repository.BorrowRecordRepository;
import tr.com.getir.getirfinalcase.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Order(3)
public class BorrowRecordDataInitializer implements CommandLineRunner {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (borrowRecordRepository.count() == 0) {
            User user = userRepository.findByEmail("richard@example.com").orElseThrow();

            // Book for overdue borrow record
            Book overdueBook = bookRepository.findByIsbn("9786053609776").orElseThrow();
            overdueBook.setAvailability(false);
            bookRepository.save(overdueBook);

            BorrowRecord overdue = BorrowRecord.builder()
                    .user(user)
                    .book(overdueBook)
                    .borrowDate(LocalDate.now().minusDays(20))
                    .dueDate(LocalDate.now().minusDays(6))
                    .returnDate(null)
                    .build();

            // Book assigned to a currently active borrow record
            Book activeBook = bookRepository.findByIsbn("9786053758962").orElseThrow();
            activeBook.setAvailability(false);
            bookRepository.save(activeBook);

            BorrowRecord active = BorrowRecord.builder()
                    .user(user)
                    .book(activeBook)
                    .borrowDate(LocalDate.now())
                    .dueDate(LocalDate.now().plusDays(14))
                    .returnDate(null)
                    .build();

            borrowRecordRepository.saveAll(List.of(overdue, active));
        }
    }
}
