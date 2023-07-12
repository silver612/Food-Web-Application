package com.example.demo.exceptionHandler;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.UnauthorizedAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler({ResourceNotFoundException.class, UnauthorizedAccessException.class})
    public ResponseEntity<String> handleResourceNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<>(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(ConversionFailedException.class)
    public ResponseEntity<String> handleConversionFailed(RuntimeException ex){
        ex.printStackTrace();
        return new ResponseEntity<>("Invalid Data For Conversion", HttpStatus.BAD_REQUEST);
    }
}
