package tr.com.getir.getirfinalcase.service;

import tr.com.getir.getirfinalcase.model.entity.User;

public interface BorrowRecordService {
    void borrowBook(User user, Long bookId);
}
