package ru.asmisloff.studyplatform.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.asmisloff.studyplatform.dto.ApiError;
import ru.asmisloff.studyplatform.exceptions.ResourceNotFoundException;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler({ NoSuchElementException.class, ResourceNotFoundException.class })
    public ResponseEntity<ApiError> noSuchElementExceptionHandler(RuntimeException ex) {
        return new ResponseEntity<>(
            new ApiError(ex.getMessage(), now()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        String errorMessages = e.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining("\n"));
        return new ResponseEntity<>(
            new ApiError(e.getNestedPath() + errorMessages, now()),
            HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    private String now() {
        return OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}
