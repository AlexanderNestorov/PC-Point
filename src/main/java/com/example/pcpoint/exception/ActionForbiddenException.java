package com.example.pcpoint.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ActionForbiddenException extends RuntimeException {

    public ActionForbiddenException() {
        super();
    }

    public ActionForbiddenException(String message) {
        super(message);
    }

}
