package com.kosaf.core.api.bbs.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.bbs.domain.Bbs;
import com.kosaf.core.api.bbs.value.DelYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BbsDetailDTO {

  // BBS 상세 조회를 위한 DTO

  private Long bbsSeq;

  private Long fileSeq;
  private String title;
  private String contents;
  private int inqCnt;

  @Enumerated(EnumType.STRING)
  private DelYn delYn;

  private LocalDateTime regDt;
  private Long rgtrSysId;

  private String rgtrSysNm;

  public static BbsDetailDTO of(Bbs bbs) {

    return BbsDetailDTO
        .builder()
        .bbsSeq(bbs.getBbsSeq())
        .fileSeq(bbs.getFileSeq())
        .title(bbs.getTitle())
        .contents(bbs.getContents())
        .inqCnt(bbs.getInqCnt())
        .delYn(bbs.getDelYn())
        .regDt(bbs.getRegDt())
        .rgtrSysId(bbs.getRgtrSysId())
        .build();
  }

}
