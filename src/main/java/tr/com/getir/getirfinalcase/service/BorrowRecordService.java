package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.dto.response.BorrowRecordWithUserResponse;
import tr.com.getir.getirfinalcase.model.dto.response.BorrowRecordsResponse;
import tr.com.getir.getirfinalcase.model.entity.User;

import java.util.List;

public interface BorrowRecordService {
    void borrowBook(User user, Long bookId);

    List<BorrowRecordsResponse> getBorrowRecordsByUserId(Long userId);

    void returnBook(Long borrowRecordId, Long id);

    List<BorrowRecordWithUserResponse> getOverdueRecords();
}
