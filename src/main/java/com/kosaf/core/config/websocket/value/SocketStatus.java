package com.kosaf.core.config.websocket.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocketStatus implements CodeType {

  ON("실행"), OFF("종료"), RUN("실행중");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}
