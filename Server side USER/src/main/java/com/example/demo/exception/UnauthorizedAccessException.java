package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedAccessException extends RuntimeException{
    
    public UnauthorizedAccessException(){
        super("Unauthorized To Access");
    }

    public UnauthorizedAccessException(String message){
        super(message);
    }

    public HttpStatus getStatus(){
        return HttpStatus.UNAUTHORIZED;
    }
}
