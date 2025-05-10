package tr.com.getir.getirfinalcase.exception;

public class UserHasOverdueRecordException extends RuntimeException{
    public UserHasOverdueRecordException(String message) {
        super(message);
    }
}
