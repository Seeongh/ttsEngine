package com.kosaf.core.api.log.presentation;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.log.application.LoginLogService;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogDTO;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogDetailDTO;
import com.kosaf.core.api.log.application.dto.loginlog.LoginLogListParam;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.ClientInfoUtil;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginLogRestController {

  private final LoginLogService loginLogService;

  @GetMapping("/loginlog")
  public ApiResponse<PageResult<LoginLogDTO>> findAll(LoginLogListParam param) {

    return loginLogService.findAll(param);
  }

  @GetMapping("/loginlog/{logSeq}")
  public ApiResponse<LoginLogDetailDTO> findBylogSeq(@PathVariable Long logSeq) {

    return loginLogService.findBylogSeq(logSeq);
  }

  /*
   * connect-info test
   */
  @GetMapping("/connect-info")
  public ApiResponse<Map<String, Object>> test(HttpServletRequest request) throws UnknownHostException {

    Map<String, Object> map = new HashMap<>();
    map.put("ip", ClientInfoUtil.getIp(request));
    map.put("browser", ClientInfoUtil.getBrowser(request));
    map.put("os", ClientInfoUtil.getOs(request));

    Optional<Map<String, Object>> result = Optional.ofNullable(map);

    return result
        .map(info -> ApiResponse.of(HttpStatus.OK, info))
        .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));
  }

}
