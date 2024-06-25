package com.kosaf.core.api.log.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebLog {

  private Long logSeq;           // 로그seq PK

  private String occrrncDe;      // 발생일자
  private String occrrncTime;    // 발생시간
  private String url;            // url
  private String rqesterIp;      // 요청자 IP
  private Integer rqesterId;     // 요청자 ID

  @Builder
  public WebLog(Long logSeq, String occrrncDe, String occrrncTime, String url, String rqesterIp,
      Integer rqesterId) {
    this.logSeq = logSeq;
    this.occrrncDe = occrrncDe;
    this.occrrncTime = occrrncTime;
    this.url = url;
    this.rqesterIp = rqesterIp;
    this.rqesterId = rqesterId;
  }



}
