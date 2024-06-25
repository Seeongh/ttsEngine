package com.kosaf.core.config.websocket.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SendNm implements CodeType {

  SYSTEM("시스템"), SELF("당신"), OTHER("상대방");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}
