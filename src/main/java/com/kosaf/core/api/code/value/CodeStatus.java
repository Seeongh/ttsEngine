package com.kosaf.core.api.code.value;

import com.kosaf.core.common.CodeType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CodeStatus implements CodeType{
  // using code
  // - com_cd_group_info.use_yn
  // - com_cd_dtl_info.use_yn

  Y("사용"), N("미사용");

  private final String description;

  @Override
  public String getCode() {
    return name();
  }

}
