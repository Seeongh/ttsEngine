package com.kosaf.core.api.log.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus implements CodeType {
  // using syslog
  // com_cd_group.ERR_YN

  Y("오류"), N("정상");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}
