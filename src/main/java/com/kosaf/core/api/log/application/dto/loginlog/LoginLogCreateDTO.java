package com.kosaf.core.api.log.application.dto.loginlog;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.log.domain.LoginLog;
import com.kosaf.core.api.log.value.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class LoginLogCreateDTO {

  @Enumerated(EnumType.STRING)
  private ConnectionStatus conectSttusCd;

  private Integer conectId;
  private String conectIp;
  private String conectOs;
  private String conectBrowser;
  private String errorCd;

  public LoginLog toEntity() {
    return LoginLog
        .builder()
        .conectSttusCd(conectSttusCd)
        .conectId(conectId)
        .conectIp(conectIp)
        .conectOs(conectOs)
        .conectBrowser(conectBrowser)
        .errorCd(errorCd)
        .build();
  }
}
