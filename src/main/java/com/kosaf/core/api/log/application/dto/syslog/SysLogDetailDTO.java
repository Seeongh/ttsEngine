package com.kosaf.core.api.log.application.dto.syslog;

import org.springframework.lang.Nullable;
import com.kosaf.core.api.log.domain.SysLog;
import com.kosaf.core.api.log.value.ErrorStatus;
import com.kosaf.core.api.log.value.ProcessSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SysLogDetailDTO {

  private Long logSeq;

  private String occrrncDe;
  private String occrrncTime;
  private String logDt;
  private String trgetMenuNm;
  private String svcNm;
  private String methodNm;
  private Integer rqesterId;
  private String rqesterIp;

  private String processSeCd;

  @Nullable
  private double processTime;

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

  public static SysLogDetailDTO of(SysLog sysLog) {
    return SysLogDetailDTO
        .builder()
        .logSeq(sysLog.getLogSeq())
        .occrrncDe(sysLog.getOccrrncDe())
        .occrrncTime(sysLog.getOccrrncTime())
        .rqesterId(sysLog.getRqesterId())
        .rqesterIp(sysLog.getRqesterIp())
        .trgetMenuNm(sysLog.getTrgetMenuNm())
        .svcNm(sysLog.getSvcNm())
        .methodNm(sysLog.getMethodNm())
        .processSeCd(sysLog.getProcessSeCd().getCode())
        .processTime(sysLog.getProcessTime())
        .errorCd(sysLog.getErrorCd())
        .errorYn(sysLog.getErrorYn().getDescription())
        .build();
  }

}
