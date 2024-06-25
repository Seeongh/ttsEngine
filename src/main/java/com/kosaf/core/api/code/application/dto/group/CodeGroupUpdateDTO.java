package com.kosaf.core.api.code.application.dto.group;

import com.kosaf.core.api.code.domain.CodeGroup;
import com.kosaf.core.api.code.value.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
public class CodeGroupUpdateDTO {

  private String groupCd;
  private String cdNm;
  private String expln;
  private CodeStatus useYn;
  private Integer mdfrSysId;

  public CodeGroup toEntity() {
    return CodeGroup
        .builder()
        .groupCd(groupCd)
        .cdNm(cdNm)
        .expln(expln)
        .useYn(useYn)
        .mdfrSysId(mdfrSysId)
        .build();
  }
}
