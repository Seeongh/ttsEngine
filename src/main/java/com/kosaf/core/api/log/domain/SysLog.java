package com.kosaf.core.api.log.domain;

import com.kosaf.core.api.log.value.ErrorStatus;
import com.kosaf.core.api.log.value.ProcessSection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SysLog {

  private Long logSeq;                  // 로그seq PK

  private String occrrncDe;             // 발생일자
  private String occrrncTime;           // 발생시간
  private String rqesterIp;             // 요청자 IP
  private Integer rqesterId;            // 요청자 ID
  private String trgetMenuNm;           // 대상메뉴명
  private String svcNm;                 // 서비스명
  private String methodNm;              // 메서드명
  private ProcessSection processSeCd;   // 처리구분코드 com_cd_dtl TB [PRCS_SE]
  private Double processTime;           // 처리 시간
  private ErrorStatus errorYn;          // 오류여부 com_cd_dtl TB [ERR_YN]
  private String errorCd;               // 오류코드

  @Builder
  public SysLog(Long logSeq, String occrrncDe, String occrrncTime, String rqesterIp,
      Integer rqesterId, String trgetMenuNm, String svcNm, String methodNm, ProcessSection processSeCd,
      Double processTime, ErrorStatus errorYn, String errorCd) {
    this.logSeq = logSeq;
    this.occrrncDe = occrrncDe;
    this.occrrncTime = occrrncTime;
    this.rqesterIp = rqesterIp;
    this.rqesterId = rqesterId;
    this.trgetMenuNm = trgetMenuNm;
    this.svcNm = svcNm;
    this.methodNm = methodNm;
    this.processSeCd = processSeCd;
    this.processTime = processTime;
    this.errorYn = errorYn;
    this.errorCd = errorCd;
  }



}
