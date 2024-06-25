package com.kosaf.core.api.member.application;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import com.kosaf.core.api.member.application.dto.MemberCreateDTO;
import com.kosaf.core.api.member.application.dto.MemberDTO;
import com.kosaf.core.api.member.application.dto.MemberDetailDTO;
import com.kosaf.core.api.member.application.dto.MemberListParam;
import com.kosaf.core.api.member.application.dto.MemberUpdateDTO;
import com.kosaf.core.api.member.domain.Member;
import com.kosaf.core.api.member.infrastructure.MemberMapper;
import com.kosaf.core.api.member.value.MemberStatus;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.validation.ValidSequence;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Validated(ValidSequence.class)
public class MemberService {

  // private final MemberRepository memberRepository; for JPA
  private final MemberMapper memberMapper;

  public ApiResponse<PageResult<MemberDTO>> findAll(MemberListParam param) {

    try {
      // DTO 및 페이징 처리
      PageResult<MemberDTO> list = PageResult
          .<MemberDTO>builder()
          .page(param.getPage()) // 요청된 페이지 번호 설정
          .pageScale(param.getPageScale()) // 페이지 당 표시될 아이템 수 설정
          .totalCount(memberMapper.countAll(param)) // 전체 카운트 수
          .items(Optional
              .ofNullable(memberMapper.findAll(param))
              .orElseGet(Collections::emptyList) // null 대신 빈 리스트 반환
              .stream()
              .map(MemberDTO::of) // 각 멤버 객체를 MemberDTO로 변환
              .collect(Collectors.toList())) // 목록 조회 및 DTO 변환
          .build();

      return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  public ApiResponse<MemberDetailDTO> findById(Long memberNo) {

    if (memberNo == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    ApiResponse<Member> memberResponse = memberInfo(memberNo);

    return Optional
        .ofNullable(memberResponse.getData())
        .map(member -> ApiResponse.of(HttpStatus.OK, MemberDetailDTO.of(member)))
        // memberInfo에서 실패한 경우 원래 상태 코드를 그대로 반환
        .orElse(ApiResponse.of(HttpStatus.valueOf(memberResponse.getStatus()), null));
  }

  @Transactional
  public ApiResponse<Long> create(MemberCreateDTO createDTO) {

    if (createDTO == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      Member member = createDTO.toEntity(); // DTO를 엔티티로 변환 후 생성 메소드를 호출
      member.create();
      // memberRepository.save(member); for JPA
      Long result = memberMapper.create(member);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, result)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  @Transactional
  public ApiResponse<MemberDetailDTO> update(@Valid MemberUpdateDTO updateDTO) {

    if (updateDTO == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    // return memberRepository.save(member); for JPA
    try {
      Member member = updateDTO.toEntity();
      member.update();
      // memberRepository.save(member); for JPA
      Long result = memberMapper.update(member);
      MemberDetailDTO updatedMemberDetail = MemberDetailDTO.of(member);

      return result > 0 ? ApiResponse.of(HttpStatus.OK, updatedMemberDetail)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  @Transactional
  public ApiResponse<Void> delete(Long memberNo) {

    // memberRepository.delete(member); //hard delete for JPA
    // return memberRepository.save(memberInfo(memberNo)); // soft delete for JPA

    ApiResponse<Member> memberResponse = memberInfo(memberNo);

    // memberInfo 호출 결과가 성공이 아닌 경우, 해당 결과를 바로 반환
    if (memberResponse.getStatus() != HttpStatus.OK.value()) {
      return ApiResponse.of(HttpStatus.valueOf(memberResponse.getStatus()), null);
    }

    // memberInfo 호출이 성공한 경우, 멤버 삭제 로직 실행
    try {
      Member member = memberResponse.getData();
      // 이미 삭제 된 멤버일 경우 처리
      if (MemberStatus.N.equals(member.getMemberStatus())) {
        return ApiResponse.of(HttpStatus.NOT_FOUND, null);
      }
      member.delete();
      Long result = memberMapper.delete(member);

      return result > 0 ? ApiResponse.of(HttpStatus.NO_CONTENT, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  public ApiResponse<Member> memberInfo(Long memberNo) {

    try {
      Optional<Member> memberInfo = Optional.ofNullable(memberMapper.findById(memberNo));
      return memberInfo
          .map(member -> ApiResponse.of(HttpStatus.OK, member))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }
}
