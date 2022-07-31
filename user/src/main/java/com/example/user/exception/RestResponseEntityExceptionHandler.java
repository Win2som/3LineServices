package com.example.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFoundException(ResourceNotFoundException resourceNotFoundException,
                                                                        WebRequest webRequest){

        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND,
                resourceNotFoundException.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(message);

    }


    @ExceptionHandler( ResourceAlreadyExistException.class)
    public ResponseEntity<ErrorMessage> handleAccountAlreadyExistException(ResourceAlreadyExistException resourceAlreadyExistException,
                                                                           WebRequest webRequest){

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST,
                resourceAlreadyExistException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message);

    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorMessage> handleCustomExceptionn(CustomException  customException,
                                                                           WebRequest webRequest){

        ErrorMessage message = new ErrorMessage(HttpStatus.BAD_REQUEST,
                customException.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(message);

    }

}
