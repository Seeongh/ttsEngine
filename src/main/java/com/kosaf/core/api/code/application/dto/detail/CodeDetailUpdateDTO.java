package com.kosaf.core.api.code.application.dto.detail;

import com.kosaf.core.api.code.domain.CodeDetail;
import com.kosaf.core.api.code.value.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CodeDetailUpdateDTO {

  private String cd;
  private String groupCd;
  private String cdNm;
  private String expln;
  private String ordr;
  private CodeStatus useYn;
  private Integer mdfrSysId;

  public CodeDetail toEntity() {
    return CodeDetail
        .builder()
        .cd(cd)
        .groupCd(groupCd)
        .cdNm(cdNm)
        .expln(expln)
        .ordr(ordr)
        .useYn(useYn)
        .mdfrSysId(mdfrSysId)
        .build();
  }
}
