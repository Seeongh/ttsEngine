package com.kosaf.core.config.security.authorization.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;

/**
 * 권한 없는 리소스 접근 시 처리 handler
 */
@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {

    CustomUserDetails loginUser =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

    if (loginUser == null) {
      response.sendRedirect("/login");
    } else {
      response.sendRedirect("/error/403");
    }
  }

}
