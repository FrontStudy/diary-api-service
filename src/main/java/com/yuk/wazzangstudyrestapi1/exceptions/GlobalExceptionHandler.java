package com.yuk.wazzangstudyrestapi1.exceptions;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode.DUPLICATE_RESOURCE;
import static com.yuk.wazzangstudyrestapi1.exceptions.ErrorCode.PERSISTENCE_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { CustomException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException throw Exception : {}", e.getMessage());
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }

    @ExceptionHandler(value = { ConstraintViolationException.class, DataIntegrityViolationException.class, EntityExistsException.class, ConstraintViolationException.class})
    protected ResponseEntity<ErrorResponse> handleDataException() {
        log.error("handleDataException throw Exception : {}", DUPLICATE_RESOURCE);
        return ErrorResponse.toResponseEntity(DUPLICATE_RESOURCE);
    }

    @ExceptionHandler(value = { PersistenceException.class})
    protected ResponseEntity<ErrorResponse> handlePersistenceException() {
        log.error("PersistenceException throw Exception : {}", PERSISTENCE_ERROR);
        return ErrorResponse.toResponseEntity(PERSISTENCE_ERROR);
    }
}