package com.kosaf.core.api.log.application.dto.weblog;

import com.kosaf.core.api.log.domain.WebLog;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WebLogDetailDTO {

  private Long logSeq;

  private String logDt;         // occrrncDe + occrrncTime
  private String occrrncDe;
  private String occrrncTime;
  private String url;
  private String rqesterIp;
  private String rqesterId;
  private String loginId;
  private String userNm;

  // entity to dto
  public static WebLogDetailDTO of(WebLog webLog) {
    return WebLogDetailDTO
        .builder()
        .logSeq(webLog.getLogSeq())
        .occrrncDe(webLog.getOccrrncDe())
        .build();
  }

}
