package com.kosaf.core.api.user.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class User {

  @NonNull
  private Integer sysId; // 시스템 아이디
  @NonNull
  private String loginId; // 로그인 아이디
  @NonNull
  private String userNm; // 사용자 이름
  private String brth; // 생년월일
  @NonNull
  private String pswdEncpt; // 비밀번호 암호화
  private String telnoEncpt; // 전화번호 암호화
  private String emailAddr; // 이메일 주소
  private String sttsCd; // 상태 코드
  private LocalDateTime lastLockDt; // 최종 잠금 일시
  private Integer pswdErrNmtn; // 비밀번호 오류 횟수
  private LocalDateTime pswdChgDt; // 비밀번호 변경일시
  private Character delYn; // 삭제여부
  private LocalDateTime regDt; // 등록일시
  private Integer rgtrSysId;// 등록자 시스템 아이디
  private LocalDateTime mdfcnDt;// 수정일시
  private Integer mdfrSysId;// 수정자 시스템 아이디


  @Builder
  public User(Integer sysId, String loginId, String userNm, String pswdEncpt) {
    this.sysId = sysId;
    this.loginId = loginId;
    this.userNm = userNm;
    this.pswdEncpt = pswdEncpt;
  }


}
