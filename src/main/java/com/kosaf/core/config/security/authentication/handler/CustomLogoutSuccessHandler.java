package com.kosaf.core.config.security.authentication.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import com.kosaf.core.api.log.application.LoginLogService;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogCreateDTO;
import com.kosaf.core.api.log.value.ConnectionStatus;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 로그아웃 성공 처리 handler
 */
@Component
public class CustomLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
    implements LogoutSuccessHandler {

  @Autowired
  private LoginLogService loginLogService;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    CustomUserDetails loginUser = (CustomUserDetails) authentication.getDetails();
    createLog(request, loginUser);

    String targetUrl = determineTargetUrl(request, response, authentication);
    if (response.isCommitted()) {
      this.logger
          .debug(LogMessage
              .format("Did not redirect to %s since response already committed.", targetUrl));
      return;
    }

    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.OK.value());

    objectMapper.writeValue(response.getWriter(), ApiResponse.of(HttpStatus.OK, null));
  }

  private void createLog(HttpServletRequest request, CustomUserDetails loginUser) {

    LoginLogCreateDTO createDTO = LoginLogCreateDTO
        .builder()
        .conectSttusCd(ConnectionStatus.O)
        .conectId(loginUser.getSysId())
        .build();

    loginLogService.create(request, createDTO);
  }
}
