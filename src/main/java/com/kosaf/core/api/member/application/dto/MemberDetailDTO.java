package com.kosaf.core.api.member.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.member.domain.Member;
import com.kosaf.core.api.member.value.Gender;
import com.kosaf.core.api.member.value.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberDetailDTO {

  // Member 상세 조회를 위한 DTO

  private Long memberNo;
  private String memberName;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private String mobile;
  private String address;

  @Enumerated(EnumType.STRING)
  private MemberStatus memberStatus;

  private LocalDateTime regDt;

  public static MemberDetailDTO of(Member member) {
    return MemberDetailDTO
        .builder()
        .memberNo(member.getMemberNo())
        .memberName(member.getMemberName())
        .gender(member.getGender())
        .mobile(member.getMobile())
        .address(member.getAddress())
        .memberStatus(member.getMemberStatus())
        .regDt(member.getRegDt())
        .build();
  }

}
