package com.kosaf.core.api.file.domain;

import java.time.LocalDateTime;
import com.kosaf.core.api.file.value.DelYn;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDtl {

  // @Id
  private Long fileDtlSeq;
  private Long fileSeq;

  private int fileSn;
  private String fileStrePath;
  private String streFileNm;
  private String orignlFileNm;
  private String fileExtsn;
  private Long fileSize;
  private DelYn delYn;
  private LocalDateTime regDt;
  private Long rgtrSysId;
  private LocalDateTime mdfcnDt;
  private Long mdfrSysId;

  @Builder
  public FileDtl(Long fileDtlSeq, Long fileSeq, int fileSn, String fileStrePath, String streFileNm,
      String orignlFileNm, String fileExtsn, Long fileSize, DelYn delYn, LocalDateTime regDt,
      Long rgtrSysId, LocalDateTime mdfcnDt, Long mdfrSysId) {

    this.fileDtlSeq = fileDtlSeq;
    this.fileSeq = fileSeq;
    this.fileSn = fileSn;
    this.fileStrePath = fileStrePath;
    this.streFileNm = streFileNm;
    this.orignlFileNm = orignlFileNm;
    this.fileExtsn = fileExtsn;
    this.fileSize = fileSize;
    this.delYn = delYn;
    this.regDt = regDt;
    this.rgtrSysId = rgtrSysId;
    this.mdfcnDt = mdfcnDt;
    this.mdfrSysId = mdfrSysId;
  }

  public void create() {
    this.delYn = DelYn.N;
    this.regDt = LocalDateTime.now();
    this.mdfcnDt = LocalDateTime.now();
  }

  public void delete() {
    this.delYn = DelYn.Y;
    this.mdfcnDt = LocalDateTime.now();
  }
}
