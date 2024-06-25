package com.kosaf.core.config.security.authentication.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import com.kosaf.core.api.log.application.LoginLogService;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogCreateDTO;
import com.kosaf.core.api.log.value.ConnectionStatus;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 로그인 성공 처리 handler
 */
@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Autowired
  private LoginLogService loginLogService;

  private RequestCache requestCache = new HttpSessionRequestCache();

  private ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    this.handle(request, response, authentication);

    CustomUserDetails loginUser = (CustomUserDetails) authentication.getDetails();
    createLog(request, loginUser);

    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.OK.value());

    objectMapper.writeValue(response.getWriter(), ApiResponse.of(HttpStatus.OK, loginUser));
  }

  @Override
  protected void handle(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    SavedRequest savedRequest = this.requestCache.getRequest(request, response);

    if (savedRequest == null) {
      super.clearAuthenticationAttributes(request);
      return;
    }
    String targetUrlParameter = getTargetUrlParameter();
    if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null
        && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
      this.requestCache.removeRequest(request, response);
      super.clearAuthenticationAttributes(request);
      return;
    }
    super.clearAuthenticationAttributes(request);
  }

  private void createLog(HttpServletRequest request, CustomUserDetails loginUser) {

    LoginLogCreateDTO createDTO = LoginLogCreateDTO
        .builder()
        .conectSttusCd(ConnectionStatus.I)
        .conectId(loginUser.getSysId())
        .build();

    loginLogService.create(request, createDTO);
  }

}
