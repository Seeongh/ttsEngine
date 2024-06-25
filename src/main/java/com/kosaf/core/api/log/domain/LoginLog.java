package com.kosaf.core.api.log.domain;

import java.time.LocalDateTime;
import com.kosaf.core.api.log.value.ConnectionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginLog {

  private Long logSeq;                      // 로그seq PK

  private ConnectionStatus conectSttusCd;   // 접속상태코드 - com_cd_dtl TB [CNTN_STTS]
  private Integer conectId;                 // 접속자 id
  private String conectIp;                  // 접속 IP
  private String conectOs;                  // 접속 OS
  private String conectBrowser;             // 접속 브라우저
  private String errorCd;                   // 오류코드
  private LocalDateTime logDt;              // 로그일시

  @Builder
  public LoginLog(Long logSeq, ConnectionStatus conectSttusCd, Integer conectId, String conectIp,
      String conectOs, String conectBrowser, String errorCd, LocalDateTime logDt) {
    this.logSeq = logSeq;
    this.conectSttusCd = conectSttusCd;
    this.conectId = conectId;
    this.conectIp = conectIp;
    this.conectOs = conectOs;
    this.conectBrowser = conectBrowser;
    this.errorCd = errorCd;
    this.logDt = logDt;
  }

}
