package dev.kkoncki.bandup.commons;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ApplicationException.class)
    public ApplicationErrorResponse handleApplicationException(ApplicationException exception) {
        Map<String, String> errors = new HashMap<>();
        String description = exception.getDescription();
        if (description == null) {
            return ApplicationErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(INTERNAL_SERVER_ERROR.value())
                    .errorCode(exception.getCode())
                    .message(exception.getMessage())
                    .errors(errors)
                    .build();
        }

        return ApplicationErrorResponse.builder()
                .timestamp(Instant.now())
                .status(INTERNAL_SERVER_ERROR.value())
                .errorCode(exception.getCode())
                .message(exception.getDescription())
                .errors(errors)
                .build();
    }

    @ResponseStatus(code = INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApplicationErrorResponse handleException(Exception exception) {
        Map<String, String> errors = new HashMap<>();

        return ApplicationErrorResponse.builder()
                .timestamp(Instant.now())
                .status(INTERNAL_SERVER_ERROR.value())
                .errorCode(ErrorCode.UNKNOWN_ERROR)
                .message(exception.getMessage())
                .errors(errors)
                .build();
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApplicationErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();

        exception.getConstraintViolations().forEach(constraintViolation -> {
            String[] exceptionNodes = constraintViolation.getPropertyPath().toString().split("[.]");
            String shortExceptionPath = exceptionNodes.length > 1 ?
                    exceptionNodes[exceptionNodes.length - 1] :
                    exceptionNodes[0];
            errors.put(shortExceptionPath, constraintViolation.getMessage());
        });

        return ApplicationErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .errorCode(ErrorCode.VALIDATION_ERROR)
                .message(exception.getMessage())
                .errors(errors)
                .build();
    }
}
