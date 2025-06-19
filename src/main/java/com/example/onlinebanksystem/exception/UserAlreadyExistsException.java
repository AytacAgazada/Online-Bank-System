package com.example.onlinebanksystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends RuntimeException {
    private final String fieldName;
    private final String fieldValue;

    public UserAlreadyExistsException(String fieldName, String fieldValue) {
        super(fieldName + " '" + fieldValue + "' is already in use.");
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public UserAlreadyExistsException(String message, String fieldName, String fieldValue) {
        super(message);
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}