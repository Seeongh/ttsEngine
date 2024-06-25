package com.kosaf.core.api.member.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.member.application.dto.MemberListParam;
import com.kosaf.core.api.member.domain.Member;

// MyBatis 사용시
@Mapper
public interface MemberMapper {

  // Mybatis 연동을 위한 Mapper

  public Integer countAll(MemberListParam param);

  public List<Member> findAll(MemberListParam param);

  public Member findById(Long memberNo);

  public Long create(Member member);

  public Long update(Member member);

  public Long delete(Member member);

}
