package tr.com.getir.getirfinalcase.exception;

public class UnauthorizedBorrowReturnException extends RuntimeException{
    public UnauthorizedBorrowReturnException(String message) {
        super(message);
    }
}
