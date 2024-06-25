package com.kosaf.core.api.member.domain;

import java.time.LocalDateTime;
import com.kosaf.core.api.member.value.Gender;
import com.kosaf.core.api.member.value.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// @Entity
@Getter
@NoArgsConstructor
public class Member {

  // ex) Member Table과 동일한 구성으로 작성하는 Entity
  // @Id
  private Long memberNo;

  private String memberName;
  private Gender gender;
  private String mobile;
  private String address;
  private MemberStatus memberStatus;
  private LocalDateTime regDt;
  private LocalDateTime updtDt;
  private LocalDateTime delDt;

  @Builder
  public Member(Long memberNo, String memberName, Gender gender, String mobile, String address,
      MemberStatus memberStatus, LocalDateTime regDt) {
    this.memberNo = memberNo;
    this.memberName = memberName;
    this.gender = gender;
    this.mobile = mobile;
    this.address = address;
    this.memberStatus = memberStatus;
    this.regDt = regDt;
  }

  public void create() {
    this.memberStatus = MemberStatus.Y;
    this.regDt = LocalDateTime.now();
  }

  public void update() {
    this.memberStatus = MemberStatus.Y;
    this.updtDt = LocalDateTime.now();
  }

  public void delete() {
    this.memberStatus = MemberStatus.N;
    this.delDt = LocalDateTime.now();
  }

}
