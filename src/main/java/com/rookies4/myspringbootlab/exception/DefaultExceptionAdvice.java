package com.rookies4.myspringbootlab.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorObject> handleBusinessException(BusinessException ex) {
        ErrorObject errorObject = new ErrorObject(ex.getMessage());
        return new ResponseEntity<>(errorObject, ex.getStatus());
    }
}