package com.kosaf.core.api.log.application.dto.syslog;

import com.kosaf.core.api.log.domain.SysLog;
import com.kosaf.core.api.log.value.ErrorStatus;
import com.kosaf.core.api.log.value.ProcessSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SysLogDTO {

  private Long listNo;
  private Long logSeq;

  private String logDt;         // occrrncDe + occrrncTime
  private String occrrncDe;
  private String occrrncTime;
  private String svcNm;
  private String methodNm;

  private String processSeCd;

  private Integer rqesterId;
  private String errorYn;
  private String errorCd;
  private String loginId;
  private String userNm;

  public void setProcessSeCd(ProcessSection processSeCd) {
    this.processSeCd = processSeCd.getDescription();
  }

  public void setErrorYn(ErrorStatus errorStatus) {
    this.errorYn = errorStatus.getDescription();
  }

  // entity to dto
  public static SysLogDTO of(SysLog sysLog) {
    return SysLogDTO
        .builder()
        .logSeq(sysLog.getLogSeq())
        .occrrncDe(sysLog.getOccrrncDe())
        .occrrncTime(sysLog.getOccrrncTime())
        .svcNm(sysLog.getSvcNm())
        .methodNm(sysLog.getMethodNm())
        .processSeCd(sysLog.getProcessSeCd().getCode())
        .rqesterId(sysLog.getRqesterId())
        .errorYn(sysLog.getErrorYn().getDescription())
        .errorCd(sysLog.getErrorCd())
        .build();
  }

}
