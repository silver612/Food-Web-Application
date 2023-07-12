package com.example.demo.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends RuntimeException{
    
    public ResourceNotFoundException(String username){
        super(username + " not found");
    }

    public HttpStatus getStatus(){
        return HttpStatus.NOT_FOUND;
    }
}
