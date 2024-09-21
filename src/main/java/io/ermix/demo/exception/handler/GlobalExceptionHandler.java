package io.ermix.demo.exception.handler;

import io.ermix.demo.exception.NotFoundException;
import io.ermix.demo.utils.ExceptionDetails;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFoundException(NotFoundException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ErrorResponse.builder()
                .message("Validation error")
                .details(ExceptionDetails.getDetails(ex.getBindingResult().getAllErrors()))
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .details(ExceptionDetails.getDetails(ExceptionDetails.getDetails(ex.getConstraintViolations())))
                .build();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return ErrorResponse.builder()
                .message("Data integrity violation")
                .details(ExceptionDetails.getDetails(ex.getMostSpecificCause().getMessage()))
                .status(HttpStatus.CONFLICT.value())
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return ErrorResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
    }

}