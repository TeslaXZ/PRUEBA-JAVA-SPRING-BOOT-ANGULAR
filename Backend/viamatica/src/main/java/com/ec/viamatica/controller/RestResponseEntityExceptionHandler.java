package com.ec.viamatica.controller;

import com.ec.viamatica.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler(RolNotFoundException.class)
    public ResponseEntity<Object> handlerRolNotFoundException(RolNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());

    }
    @ExceptionHandler(IdentificacionException.class)
    public ResponseEntity<Object> handlerIdentificacionException(IdentificacionException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());

    }
    @ExceptionHandler(usernameException.class)
    public ResponseEntity<Object> handlerUsernameException(usernameException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
    @ExceptionHandler(PasswordException.class)
    public ResponseEntity<Object> handlerPasswordException(PasswordException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handlerEntityNotFoundException(EntityNotFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
    @ExceptionHandler(NoUserFoundException.class)
    public ResponseEntity<Object> NoUserFoundException(NoUserFoundException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }
    @ExceptionHandler(UserSessionException.class)
    public ResponseEntity<Object> UserSessionException(UserSessionException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ex.getMessage());
    }


}
