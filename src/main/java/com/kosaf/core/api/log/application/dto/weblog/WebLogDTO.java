package com.kosaf.core.api.log.application.dto.weblog;

import com.kosaf.core.api.log.domain.WebLog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WebLogDTO {

  private Long listNo;
  private Long logSeq;

  private String logDt;         // occrrncDe + occrrncTime
  private String occrrncDe;
  private String occrrncTime;
  private String url;
  private String rqesterIp;
  private Integer rqesterId;
  private String loginId;
  private String userNm;

  public static WebLogDTO of(WebLog webLog) {
    return WebLogDTO
        .builder()
        .logSeq(webLog.getLogSeq())
        .occrrncDe(webLog.getOccrrncDe())
        .occrrncTime(webLog.getOccrrncTime())
        .url(webLog.getUrl())
        .rqesterId(webLog.getRqesterId())
        .rqesterIp(webLog.getRqesterIp())
        .build();
  }

}
