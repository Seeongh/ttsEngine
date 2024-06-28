package com.kosaf.core.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidClientException extends RuntimeException {
    private HttpStatus status;

    public InvalidClientException(HttpStatus status) {
        this.status = status;
    }

    public InvalidClientException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public InvalidClientException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public InvalidClientException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }
}