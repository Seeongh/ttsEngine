package com.kosaf.core.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResponseException extends RuntimeException {
    private HttpStatus status;

    public ResponseException(HttpStatus status) {
        this.status = status;
    }

    public ResponseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public ResponseException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public ResponseException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }
}