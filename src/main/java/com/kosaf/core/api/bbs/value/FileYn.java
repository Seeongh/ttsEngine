package com.kosaf.core.api.bbs.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileYn implements CodeType {

  Y("있음"), N("없음");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}
