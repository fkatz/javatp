package javatp.exception;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javatp.domain.Message;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Object> exception(AuthenticationException exception) {
        return ResponseEntity.badRequest().body(new Message("Authentication error", exception.getMessage()));
    }
    
    @ExceptionHandler(value = IncompleteObjectException.class)
    public ResponseEntity<Object> exception(IncompleteObjectException exception) {
        return ResponseEntity.badRequest().body(new Message("Incomplete object error", exception.getMessage()));
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<Object> exception(EntityNotFoundException exception) {
        return ResponseEntity.badRequest().body(new Message("Entity not found", exception.getMessage()));
    }

    @ExceptionHandler(value = UsernameTakenException.class)
    public ResponseEntity<Object> exception(UsernameTakenException exception) {
        return ResponseEntity.badRequest().body(new Message("Username already in use, select another", exception.getMessage()));
    }

}