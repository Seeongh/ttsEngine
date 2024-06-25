package com.kosaf.core.api.bbs.domain;

import java.time.LocalDateTime;
import com.kosaf.core.api.bbs.value.DelYn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Bbs {

  private final static int INQ_CNT = 0;

  // @Id
  private Long bbsSeq;

  private Long fileSeq;

  private String title;
  private String contents;
  private int inqCnt;
  private DelYn delYn;
  private LocalDateTime regDt;
  private Long rgtrSysId;
  private LocalDateTime mdfcnDt;
  private Long mdfrSysId;

  @Builder
  public Bbs(Long bbsSeq, Long fileSeq, String title, String contents, int inqCnt, DelYn delYn,
      LocalDateTime regDt, Long rgtrSysId, LocalDateTime mdfcnDt, Long mdfrSysId) {

    this.bbsSeq = bbsSeq;
    this.fileSeq = fileSeq;
    this.title = title;
    this.contents = contents;
    this.inqCnt = inqCnt;
    this.delYn = delYn;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
    this.mdfcnDt = mdfcnDt;
    this.mdfrSysId = mdfrSysId;
  }

  public void create(Long sysId) {
    this.inqCnt = INQ_CNT;
    this.delYn = DelYn.N;
    this.regDt = LocalDateTime.now();
    this.rgtrSysId = sysId;
    this.mdfcnDt = LocalDateTime.now();
    this.mdfrSysId = sysId;
  }

  public void update(Long sysId) {
    this.delYn = DelYn.N;
    this.mdfcnDt = LocalDateTime.now();
    this.mdfrSysId = sysId;
  }

  public void delete(Long sysId) {
    this.delYn = DelYn.Y;
    this.mdfcnDt = LocalDateTime.now();
    this.mdfrSysId = sysId;
  }
}
