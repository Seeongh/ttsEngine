package com.kosaf.core.api.code.application.dto.group;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import com.kosaf.core.api.code.domain.CodeGroup;
import com.kosaf.core.api.code.value.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CodeGroupDetailDTO {

  private String groupCd;
  private String cdNm;
  private String expln;

  private CodeStatus useYn;

  private String regDt;

  public void setRegDt(LocalDateTime regDt) {
    this.regDt = regDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
  }

  public static CodeGroupDetailDTO of(CodeGroup codeGroup) {
    return CodeGroupDetailDTO
        .builder()
        .groupCd(codeGroup.getGroupCd())
        .cdNm(codeGroup.getCdNm())
        .expln(codeGroup.getExpln())
        .useYn(codeGroup.getUseYn())
        .regDt(codeGroup.getRegDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        .build();
  }

}
