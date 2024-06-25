package com.kosaf.core.api.code.application.dto.detail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.kosaf.core.api.code.value.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CodeDetailDTO {

  private String groupCd;
  private String groupCdNm;
  private String cd;
  private String cdNm;
  private String expln;
  private String ordr;

  private CodeStatus useYn;

  private String regDt;


  public void setRegDt(LocalDateTime regDt) {
    this.regDt = regDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

}
