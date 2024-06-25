package com.kosaf.core.api.author.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Author {

  private String authorId;// 권한 아이디
  private String authorNm;// 권한명
  private String expln;// 설명
  private String pattern;// 패턴
  private LocalDateTime regDt; // 등록일시
  private int rgtrSysId;// 등록자 시스템 아이디
  private LocalDateTime mdfcnDt;// 수정일시
  private int mdfrSysId;// 수정자 시스템 아이디

  @Builder
  public Author(String authorId, String authorNm, String pattern, LocalDateTime regDt,
      int rgtrSysId, LocalDateTime mdfcnDt, int mdfrSysId) {
    this.authorId = authorId;
    this.authorNm = authorNm;
    this.pattern = pattern;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
    this.mdfcnDt = mdfcnDt;
    this.mdfrSysId = mdfrSysId;
  }
}
