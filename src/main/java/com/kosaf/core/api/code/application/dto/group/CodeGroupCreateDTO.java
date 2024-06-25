package com.kosaf.core.api.code.application.dto.group;

import com.kosaf.core.api.code.domain.CodeGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CodeGroupCreateDTO {

  private String groupCd;
  private String cdNm;
  private String expln;
  private Integer rgtrSysId;

  public CodeGroup toEntity() {
    return CodeGroup
        .builder()
        .groupCd(groupCd)
        .cdNm(cdNm)
        .expln(expln)
        .rgtrSysId(rgtrSysId)
        .build();
  }

}
