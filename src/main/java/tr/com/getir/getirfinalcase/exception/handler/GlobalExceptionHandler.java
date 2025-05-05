package tr.com.getir.getirfinalcase.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tr.com.getir.getirfinalcase.exception.EmailAlreadyExistException;
import tr.com.getir.getirfinalcase.exception.errormessages.GeneralErrorMessage;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<GeneralErrorMessage> handleEmailAlreadyExistException(EmailAlreadyExistException exception){
        return new ResponseEntity<>(new GeneralErrorMessage(false, exception.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralErrorMessage> handleValidationException(MethodArgumentNotValidException exception) {
        StringBuilder errorMessage = new StringBuilder();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errorMessage.append(error.getField()).append(": ").append(error.getDefaultMessage())
        );

        return new ResponseEntity<>(
                new GeneralErrorMessage(false, errorMessage.toString(), LocalDateTime.now()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GeneralErrorMessage> handleEnumBindingError(HttpMessageNotReadableException ex) {
        String message = "Invalid request payload";

        Throwable cause = ex.getMostSpecificCause();
        if (cause != null && cause.getMessage() != null && cause.getMessage().contains("Enum class")) {
            message = "Invalid enum value";
        }

        return ResponseEntity
                .badRequest()
                .body(new GeneralErrorMessage(false, message, LocalDateTime.now()));
    }


}
