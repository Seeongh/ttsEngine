package com.kosaf.core.api.log.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ConnectionStatus implements CodeType {
  // using loginLog
  // com_cd_group.CNTN_STTS

  I("로그인"), O("로그아웃");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }


}
