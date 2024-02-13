package com.leonardo.ecommerce.infra.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ErrorHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoContentException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<String> error201() {
        log.error("NO_CONTENT: No Content!?.");
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> Error404(ResourceNotFoundException exception) {
        log.error("NOT_FOUND:  No found!? {}.", exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> Error400(HttpMessageNotReadableException ex) {
        log.error("BAD_REQUEST: Body request is invalid or empty.");
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> ErrorBadCredentials() {
        log.error("UNAUTHORIZED: Invalid credentials.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> ErrorAuthentication() {
        log.error("UNAUTHORIZED: Auth failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Auth failed");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> AccessDenied() {
        log.error("FORBIDDEN: Access denied");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> error500(Exception exception) {
        log.error("INTERNAL_SERVER_ERROR: {}", exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> error400(MethodArgumentNotValidException exception) {
        log.error("BAD_REQUEST: {}", exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + exception.getMessage());
    }

    @ExceptionHandler(HttpClientErrorException.UnprocessableEntity.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<String> error422(HttpClientErrorException.UnprocessableEntity exception) {
        log.error("UNPROCESSABLE_ENTITY: {}", exception.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + exception.getMessage());
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String ErrorMessage =
                "An error occurred while processing your request, The provided value already exists in the database.";
        log.error("CONFLICT: {}", ErrorMessage);

        return new ResponseEntity<>("409", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PasswordNotMatchesException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> PasswordNotMatchesException(PasswordNotMatchesException exception) {
        String ErrorMessage = "Password update not authorized. Please check your credentials and ensure the new password is different from the current password.";
        log.error("UNAUTHORIZED: {}", ErrorMessage);

        return new ResponseEntity<>(ErrorMessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleInternalAuthenticationServiceException
            (InternalAuthenticationServiceException exception) {
        String ErrorMessage = "An error occurred while authenticating the user, Please try again later";
        log.error("INTERNAL_SERVER_ERROR: {}", ErrorMessage);

        return new ResponseEntity<>(ErrorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }

    }

    public static class NoContentException extends RuntimeException {
        public NoContentException() {
            super();
        }
    }

    public static class DataIntegrityViolationException extends RuntimeException {
        public DataIntegrityViolationException() {
            super();
        }
    }

    public static class PasswordNotMatchesException extends RuntimeException {
        public PasswordNotMatchesException() {
            super();
        }
    }
}
