package com.kosaf.core.api.member.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import com.kosaf.core.api.member.domain.Member;
import com.kosaf.core.api.member.value.Gender;
import com.kosaf.core.api.member.value.MemberStatus;
import com.kosaf.core.config.validation.ValidGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateDTO {

  // Member Create를 위한 DTO
  @NotEmpty(message = "멤버 이름은 필수 입력값입니다", groups = ValidGroups.First.class)
  private String memberName;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @NotEmpty(message = "전화번호는 필수 입력값입니다", groups = ValidGroups.Third.class)
  private String mobile;

  @NotEmpty(message = "주소는 필수 입력값입니다", groups = ValidGroups.Second.class)
  private String address;

  @Enumerated(EnumType.STRING)
  private MemberStatus memberStatus;

  private LocalDateTime regDt;

  public Member toEntity() {
    return Member
        .builder()
        .memberName(memberName)
        .gender(gender)
        .mobile(mobile)
        .address(address)
        .build();
  }

}
