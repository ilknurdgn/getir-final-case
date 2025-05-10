package tr.com.getir.getirfinalcase.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tr.com.getir.getirfinalcase.exception.*;
import tr.com.getir.getirfinalcase.exception.errormessages.GeneralErrorMessage;
import org.springframework.context.support.DefaultMessageSourceResolvable;


import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<GeneralErrorMessage> handleEmailAlreadyExistException(EmailAlreadyExistException exception){
        return new ResponseEntity<>(new GeneralErrorMessage(false, exception.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralErrorMessage> handleValidationException(MethodArgumentNotValidException exception) {
        String errorMessages = exception.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                new GeneralErrorMessage(false, errorMessages, LocalDateTime.now()),
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

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<GeneralErrorMessage> handleInvalidCredentialsException(InvalidCredentialsException exception){
        return new ResponseEntity<>(new GeneralErrorMessage(false, exception.getMessage(), LocalDateTime.now()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<GeneralErrorMessage> handleEntityNotFoundException(EntityNotFoundException exception){
        return new ResponseEntity<>(new GeneralErrorMessage(false, exception.getMessage(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GeneralErrorMessage> handleAccessDeniedException(AccessDeniedException exception) {
        return new ResponseEntity<>(new GeneralErrorMessage(false, "Access denied. You are not authorized for this action.", LocalDateTime.now()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<GeneralErrorMessage> handleDuplicateIsbnException(DuplicateIsbnException exception){
        return new ResponseEntity<>(new GeneralErrorMessage(false, exception.getMessage(), LocalDateTime.now()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserHasOverdueRecordException.class)
    public ResponseEntity<GeneralErrorMessage> handleUserHasOverdueRecordException(UserHasOverdueRecordException exception){
        return new ResponseEntity<>(new GeneralErrorMessage(false, exception.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public ResponseEntity<GeneralErrorMessage> handleBookNotAvailableException(BookNotAvailableException exception){
        return new ResponseEntity<>(new GeneralErrorMessage(false, exception.getMessage(), LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }


}
