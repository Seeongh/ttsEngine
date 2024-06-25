package com.kosaf.core.api.member.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberStatus implements CodeType {

  Y("사용중"), N("삭제된 계정");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }

}
