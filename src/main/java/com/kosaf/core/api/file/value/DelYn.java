package com.kosaf.core.api.file.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DelYn implements CodeType {

  Y("삭제"), N("미삭제");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }
}
