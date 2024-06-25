package com.kosaf.core.api.member.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.member.domain.Member;
import com.kosaf.core.api.member.value.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberDTO {

  // Member 목록 조회를 위한 DTO

  private Long memberNo;
  private String memberName;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private String mobile;
  private String address;

  private LocalDateTime regDt;

  public static MemberDTO of(Member member) {
    return MemberDTO
        .builder()
        .memberNo(member.getMemberNo())
        .memberName(member.getMemberName())
        .gender(member.getGender())
        .mobile(member.getMobile())
        .address(member.getAddress())
        .regDt(member.getRegDt())
        .build();
  }

}
