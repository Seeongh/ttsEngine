package com.kosaf.core.api.code.application.dto.detail;

import com.kosaf.core.api.code.domain.CodeDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CodeDetailCreateDTO {

  private String cd;
  private String groupCd;
  private String cdNm;
  private String expln;
  private String ordr;
  private Integer rgtrSysId;

  public CodeDetail toEntity() {
    return CodeDetail
      .builder()
      .cd(cd)
      .groupCd(groupCd)
      .cdNm(cdNm)
      .expln(expln)
      .ordr(ordr)
      .rgtrSysId(rgtrSysId)
      .build();
  }
}
