package com.kosaf.core.api.member.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender implements CodeType {

  M("남"), F("여");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }

}
