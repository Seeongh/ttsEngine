package com.kosaf.core.api.bbs.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import com.kosaf.core.api.bbs.domain.Bbs;
import com.kosaf.core.api.bbs.value.DelYn;
import com.kosaf.core.config.validation.ValidGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BbsCreateDTO {

  // BBS Create를 위한 DTO

  private Long fileSeq;

  @NotEmpty(message = "제목은 필수 입력 항목입니다.", groups = ValidGroups.First.class)
  private String title;

  @NotEmpty(message = "내용은 필수 입력 항목입니다.", groups = ValidGroups.Second.class)
  private String contents;

  private int inqCnt;

  @Enumerated(EnumType.STRING)
  private DelYn delYn;

  private LocalDateTime regDt;
  private Long rgtrSysId;
  private LocalDateTime mdfcnDt;
  private Long mdfrSysId;

  public Bbs toEntity() {
    return Bbs.builder().fileSeq(fileSeq).title(title).contents(contents).build();
  }
}
