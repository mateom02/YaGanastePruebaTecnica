package com.prueba.tarjetas.exceptions;

import com.prueba.tarjetas.util.Result;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Result> handleNotFound(NotFoundException ex) {
        Result result = new Result();
        result.correct = false;
        result.errorMessage = ex.getMessage();
        result.ex = ex;
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Result> handleValidation(ValidationException ex) {
        Result result = new Result();
        result.correct = false;
        result.errorMessage = ex.getMessage();
        result.ex = ex;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Result> handleDuplicate(DuplicateResourceException ex) {
        Result result = new Result();
        result.correct = false;
        result.errorMessage = ex.getMessage();
        result.ex = ex;
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Result> handleDataIntegrity(DataIntegrityViolationException ex) {
        Result result = new Result();
        result.correct = false;
        result.errorMessage = "Violación de integridad de datos: " + getRootCause(ex);
        result.ex = ex;
        return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result> handleBusiness(BusinessException ex) {
        Result result = new Result();
        result.correct = false;
        result.errorMessage = ex.getMessage();
        result.ex = ex;
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result> handleGeneral(Exception ex) {
        Result result = new Result();
        result.correct = false;
        result.errorMessage = "Error interno del servidor: " + ex.getMessage();
        result.ex = ex;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }

    private String getRootCause(Throwable ex) {
        Throwable cause = ex;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }

}
