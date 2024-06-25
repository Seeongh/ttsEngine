package com.kosaf.core.api.log.application.dto.syslog;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.log.domain.SysLog;
import com.kosaf.core.api.log.value.ErrorStatus;
import com.kosaf.core.api.log.value.ProcessSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class SysLogCreateDTO {

  private String rqesterIp;
  private Integer rqesterId;
  private String trgetMenuNm;
  private String svcNm;
  private String methodNm;

  @Enumerated(EnumType.STRING)
  private ProcessSection processSeCd;

  private double processTime;

  @Enumerated(EnumType.STRING)
  private ErrorStatus errorYn;

  private String errorCd;

  public SysLog toEntity() {
    return SysLog
        .builder()
        .rqesterIp(rqesterIp)
        .rqesterId(rqesterId)
        .trgetMenuNm(trgetMenuNm)
        .svcNm(svcNm)
        .methodNm(methodNm)
        .processSeCd(processSeCd)
        .processTime(processTime)
        .errorYn(errorYn)
        .errorCd(errorCd)
        .build();
  }

}
