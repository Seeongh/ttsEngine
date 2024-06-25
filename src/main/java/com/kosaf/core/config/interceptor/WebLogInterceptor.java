package com.kosaf.core.config.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.kosaf.core.api.log.application.WebLogService;
import com.kosaf.core.api.log.application.dto.weblog.WebLogCreateDTO;
import com.kosaf.core.common.ClientInfoUtil;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WebLogInterceptor implements HandlerInterceptor {

  private final WebLogService webLogService;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

    String reqURL = request.getRequestURI();
    Object authentication = SecurityContextHolder.getContext().getAuthentication().getDetails();

    // 로그인 객체 log만 저장. 없을 시 AccessDeniedHandler에서 redirect 처리
    if ( authentication instanceof CustomUserDetails ) {
      CustomUserDetails userInfo = (CustomUserDetails) authentication;

      WebLogCreateDTO createDTO = WebLogCreateDTO
          .builder()
          .url(reqURL)
          .rqesterIp(ClientInfoUtil.getIp(request))
          .rqesterId(userInfo.getSysId())
          .build();

      webLogService.create(request, createDTO);
    }
  }
}
