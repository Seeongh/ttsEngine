package com.kosaf.core.api.log.application.dto.loginlog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.log.value.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginLogDTO {

  // 목록 조회
  private Long listNo;
  private Long logSeq;

  @Enumerated(EnumType.STRING)
  private ConnectionStatus conectSttusCd;

  private Integer conectId;
  private String conectIp;
  private String conectBrowser;
  private String logDt;
  private String loginId;
  private String userNm;

  public void setLogDt(LocalDateTime logDt) {
    this.logDt = logDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}
