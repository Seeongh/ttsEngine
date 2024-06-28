package com.kosaf.core.config.exception;

import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.exception.ResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseExceptionHandler {

    @ExceptionHandler(ResponseException.class)
    public ApiResponse<String> handleResponseException(ResponseException e) {
        String message = getMessage(e.getStatus());

        log.info("responseException message = {}", e.getMessage());
        if(e.getStatus() != HttpStatus.OK) {
            if(e.getMessage() != null) {
                return new ApiResponse<String>(e.getStatus(), e.getMessage(), "");
            }
            return new ApiResponse<String>(e.getStatus(),
                    message, "");
        }
        return new ApiResponse<String>(HttpStatus.OK,
                message,"");
    }

    private String getMessage(HttpStatus status) {
        switch (status) {
            case BAD_REQUEST: // 400
                return "잘못된 요청입니다.";
            case CONFLICT: // 409
                return "중복이 있습니다.";
            case NOT_ACCEPTABLE: // 500
                return "지정된 형식이 아닙니다.";
            default :
                return "오류가 발생했습니다.";
        }
    }
}
