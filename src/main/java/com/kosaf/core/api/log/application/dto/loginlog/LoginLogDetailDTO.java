package com.kosaf.core.api.log.application.dto.loginlog;

import java.time.format.DateTimeFormatter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.log.domain.LoginLog;
import com.kosaf.core.api.log.value.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class LoginLogDetailDTO {

  private Long logSeq;

  @Enumerated(EnumType.STRING)
  private ConnectionStatus conectSttusCd;

  private Integer conectId;
  private String conectIp;
  private String conectOs;
  private String conectBrowser;
  private String errorCd;
  private String logDt;
  private String loginId;
  private String userNm;

  public static LoginLogDetailDTO of(LoginLog loginLog) {
    return LoginLogDetailDTO
        .builder()
        .logSeq(loginLog.getLogSeq())
        .conectSttusCd(loginLog.getConectSttusCd())
        .conectId(loginLog.getConectId())
        .conectIp(loginLog.getConectIp())
        .conectOs(loginLog.getConectOs())
        .conectBrowser(loginLog.getConectBrowser())
        .errorCd(loginLog.getErrorCd())
        .logDt(loginLog.getLogDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        .build();
  }

}

