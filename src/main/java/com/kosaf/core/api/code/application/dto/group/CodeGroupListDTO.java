package com.kosaf.core.api.code.application.dto.group;

import com.kosaf.core.api.code.value.CodeStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CodeGroupListDTO {

  private String listNo;
  private String groupCd;
  private String cdNm;
  private CodeStatus useYn;

}
