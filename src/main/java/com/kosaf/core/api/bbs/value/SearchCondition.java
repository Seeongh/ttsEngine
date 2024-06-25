package com.kosaf.core.api.bbs.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchCondition implements CodeType {

  TITLE("제목"), CONTENTS("내용");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}
