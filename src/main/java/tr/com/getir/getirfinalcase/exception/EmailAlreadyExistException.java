package tr.com.getir.getirfinalcase.exception;

public class EmailAlreadyExistException extends RuntimeException{

    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
