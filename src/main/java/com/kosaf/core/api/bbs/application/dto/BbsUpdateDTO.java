package com.kosaf.core.api.bbs.application.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
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
public class BbsUpdateDTO {

  // BBS update를 위한 DTO

  @Min(value = 0, message = "요청이 올바르지 않습니다.", groups = ValidGroups.First.class)
  private Long bbsSeq;

  private Long fileSeq;

  @NotEmpty(message = "제목은 필수 입력 항목입니다.", groups = ValidGroups.Second.class)
  private String title;

  @NotEmpty(message = "내용은 필수 입력 항목입니다.", groups = ValidGroups.Third.class)
  private String contents;

  @Enumerated(EnumType.STRING)
  private DelYn delYn;

  public Bbs toEntity() {
    return Bbs
        .builder()
        .bbsSeq(bbsSeq)
        .fileSeq(fileSeq)
        .title(title)
        .contents(contents)
        .delYn(delYn)
        .build();
  }
}
