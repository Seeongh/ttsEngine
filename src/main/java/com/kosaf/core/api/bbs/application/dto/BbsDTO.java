package com.kosaf.core.api.bbs.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.bbs.domain.Bbs;
import com.kosaf.core.api.bbs.value.FileYn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BbsDTO {

  private Integer rownum;
  private Long bbsSeq;

  private Long fileSeq;
  private String title;
  private String contents;
  private int inqCnt;
  private LocalDateTime regDt;
  private Long rgtrSysId;

  private String rgtrSysNm;

  @Enumerated(EnumType.STRING)
  private FileYn fileYn;

  public static BbsDTO of(Bbs bbs) {
    return BbsDTO
        .builder()
        .bbsSeq(bbs.getBbsSeq())
        .fileSeq(bbs.getFileSeq())
        .title(bbs.getTitle())
        .contents(bbs.getContents())
        .inqCnt(bbs.getInqCnt())
        .regDt(bbs.getRegDt())
        .rgtrSysId(bbs.getRgtrSysId())
        .fileYn((bbs.getFileSeq() == null) ? FileYn.N : FileYn.Y)
        .build();
  }
}
