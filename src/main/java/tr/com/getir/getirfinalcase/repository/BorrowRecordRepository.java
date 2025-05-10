package tr.com.getir.getirfinalcase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.com.getir.getirfinalcase.model.entity.BorrowRecord;
import tr.com.getir.getirfinalcase.model.entity.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {

    boolean existsByUserAndDueDateBeforeAndReturnDateIsNull(User user, LocalDate now);

    List<BorrowRecord> findByUserId(Long userId);
}
