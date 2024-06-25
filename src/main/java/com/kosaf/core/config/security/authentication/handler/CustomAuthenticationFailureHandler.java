package com.kosaf.core.config.security.authentication.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.kosaf.core.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 로그인 실패 처리 handler
 */
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {

    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());

    objectMapper
        .writeValue(response.getWriter(),
            new ApiResponse<>(HttpStatus.UNAUTHORIZED, exception.getMessage(), null));
  }

}
