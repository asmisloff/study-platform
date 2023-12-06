package ru.amisfloff.studyplatform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.amisfloff.studyplatform.dto.ApiError;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiError> noSuchElementExceptionHandler(NoSuchElementException ex) {
        return new ResponseEntity<>(
            new ApiError(
                ex.getMessage(),
                OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            ),
            HttpStatus.NOT_FOUND
        );
    }
}
