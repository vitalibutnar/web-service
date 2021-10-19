package com.endava.webservice.controllers;

import com.endava.webservice.exeption.DataBaseConnectionException;
import com.endava.webservice.exeption.IllegalFieldValueException;
import com.endava.webservice.exeption.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestExceptionController {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> validationExceptions(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return getErrorResponseEntity(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, String>> noDataFoundExceptions(NoDataFoundException exception) {
        return getErrorResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(IllegalFieldValueException.class)
    public ResponseEntity<Map<String, String>> validationExceptions(IllegalFieldValueException exception) {
        return getErrorResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(DataBaseConnectionException.class)
    public ResponseEntity<Map<String, String>> databaseConnectionException(DataBaseConnectionException exception) {
        return getErrorResponseEntity(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private ResponseEntity<Map<String, String>> getErrorResponseEntity(HttpStatus code, String message) {
        Map<String, String> errors = new HashMap<>();
        errors.put("timestamp", LocalDateTime.now().toString());
        errors.put("code", code.toString());
        errors.put("reason", message);
        return new ResponseEntity<>(errors, code);
    }
}