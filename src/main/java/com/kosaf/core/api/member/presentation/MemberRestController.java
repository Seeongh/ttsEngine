package com.kosaf.core.api.member.presentation;

import javax.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.member.application.MemberService;
import com.kosaf.core.api.member.application.dto.MemberCreateDTO;
import com.kosaf.core.api.member.application.dto.MemberDTO;
import com.kosaf.core.api.member.application.dto.MemberDetailDTO;
import com.kosaf.core.api.member.application.dto.MemberListParam;
import com.kosaf.core.api.member.application.dto.MemberUpdateDTO;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.validation.ValidSequence;
import lombok.RequiredArgsConstructor;

/**
 * @Validated <br>
 *            "@PathVariable" "@RequestParam" 으로 받는 객체에 javax.validation.constraints 사용을 위해 추가.<br>
 *            "@RequestBody" 에 "@Validated" 사용하는 경우에는 추가할 필요 없음.
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberRestController {

  // RestAPI Controller

  private final MemberService memberService;
  private static final Logger log = LoggerFactory.getLogger(MemberRestController.class);

  @GetMapping("/members")
  public ApiResponse<PageResult<MemberDTO>> findAll(@RequestBody MemberListParam param) {

    return memberService.findAll(param);
  }

  @GetMapping("/member/{memberNo}")
  public ApiResponse<MemberDetailDTO> findById(@PathVariable @Min(0) Long memberNo) {

    return memberService.findById(memberNo);
  }

  @PostMapping("/member")
  public ApiResponse<Long> create(
      @RequestBody @Validated(ValidSequence.class) MemberCreateDTO createDTO) {

    return memberService.create(createDTO);
  }

  @PatchMapping("/member")
  public ApiResponse<MemberDetailDTO> update(@RequestBody MemberUpdateDTO updateDTO) {

    return memberService.update(updateDTO);
  }

  @DeleteMapping("/member/{memberNo}")
  public ApiResponse<Void> delete(
      @PathVariable @Min(value = 0, message = "memberNo는 0보다 커야합니다.") Long memberNo) {

    return memberService.delete(memberNo);
  }

}
