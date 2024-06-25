package com.kosaf.core.api.log.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProcessSection implements CodeType {
  // using sysLog
  // com_cd_group.PRCS_SE

  C("생성"), R("조회"), U("수정"), D("삭제");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }

}
