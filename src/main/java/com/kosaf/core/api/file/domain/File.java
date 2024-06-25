package com.kosaf.core.api.file.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class File {

  // @Id
  private Long fileSeq;

  private LocalDateTime regDt;
  private Long rgtrSysId;

  @Builder
  public File(Long fileSeq, LocalDateTime regDt, Long rgtrSysId) {
    this.fileSeq = fileSeq;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
  }

  public void create() {
    this.regDt = LocalDateTime.now();
  }
}
