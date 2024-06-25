package com.kosaf.core.api.member.application.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.kosaf.core.api.member.value.Gender;
import com.kosaf.core.api.member.value.MemberStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 검색조건 등 parameter를 정의
 */

@Getter
@NoArgsConstructor
public class MemberListParam {

  // Member 목록 조회를 위한 Parameter 정의

  private final static int PAGE = 1;
  private final static int DEFAULT_PAGE_SCALE = 10;

  /**
   * 조회페이지
   */
  private Integer page = PAGE;

  /**
   * 페이지당 게시물수
   */
  private Integer pageScale = DEFAULT_PAGE_SCALE;

  private String memberName;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Enumerated(EnumType.STRING)
  private MemberStatus memberStatus = MemberStatus.Y;

  @Builder
  public MemberListParam(Integer page, Integer pageScale, String memberName, Gender gender,
      MemberStatus memberStatus) {

    this.page = (page == null) ? PAGE : page;
    this.pageScale = (pageScale == null) ? DEFAULT_PAGE_SCALE : pageScale;
    this.memberName = memberName;
    this.gender = gender;
    this.memberStatus = (memberStatus == null) ? MemberStatus.Y : memberStatus;
  }

}
