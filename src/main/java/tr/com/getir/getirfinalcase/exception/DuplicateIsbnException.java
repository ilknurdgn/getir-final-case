package tr.com.getir.getirfinalcase.exception;

public class DuplicateIsbnException extends RuntimeException{
    public DuplicateIsbnException(String message) {
        super(message);
    }
}
