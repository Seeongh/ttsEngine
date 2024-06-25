package com.kosaf.core.api.member.application.dto;

import java.time.LocalDateTime;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
public class MemberUpdateDTO {

  @NotEmpty(message = "a는 필수 입력값입니다", groups = ValidGroups.First.class)
  private String a;

  @NotNull(message = "b는 필수 입력값입니다", groups = ValidGroups.Second.class)
  @Min(value = 0, message = "b는 0보다 커야합니다.", groups = ValidGroups.Third.class)
  private Integer b;

  @NotEmpty(message = "c는 필수 입력값입니다", groups = ValidGroups.Fourth.class)
  private String c;

  @NotNull(message = "b는 필수 입력값입니다", groups = ValidGroups.Fifth.class)
  @Min(value = 0, message = "d는 0보다 커야합니다.", groups = ValidGroups.Sixth.class)
  private Integer d;

  @NotEmpty(message = "e는 필수 입력값입니다", groups = ValidGroups.Seventh.class)
  private String e;

  // Member Update를 위한 DTO
  @Min(value = 0, message = "멤버 번호는 0보다 커야합니다.", groups = ValidGroups.Eighth.class)
  private Long memberNo;

  @NotEmpty(message = "멤버 이름은 필수 입력값입니다", groups = ValidGroups.Ninth.class)
  private String memberName;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  private String mobile;

  private String address;

  @Enumerated(EnumType.STRING)
  private MemberStatus memberStatus;

  private LocalDateTime regDt;

  public Member toEntity() {
    return Member
        .builder()
        .memberNo(memberNo)
        .memberName(memberName)
        .gender(gender)
        .mobile(mobile)
        .address(address)
        .build();
  }
}
