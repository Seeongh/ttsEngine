package com.kosaf.core.api.code.presentation;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.code.application.CodeDetailService;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailCreateDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailListDTO;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailListParam;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailUpdateDTO;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CodeDetailRestController {

  private final CodeDetailService codeDetailService;

  @GetMapping("/code-detail")
  public ApiResponse<PageResult<CodeDetailListDTO>> findAll(CodeDetailListParam param) {

    return codeDetailService.findAll(param);
  }

  @GetMapping("/code-detail/{groupCd}")
  public ApiResponse<List<CodeDetailDTO>> findByCd(@PathVariable String groupCd) {

    return codeDetailService.findAllByGroupCd(groupCd);
  }

  @GetMapping("/code-detail/{groupCd}/{cd}")
  public ApiResponse<CodeDetailDTO> findByCd(@PathVariable String groupCd, @PathVariable String cd) {

    return codeDetailService.findByGroupCdAndCd(groupCd, cd);
  }

  @PostMapping("/code-detail")
  public ApiResponse<Long> create(CodeDetailCreateDTO createDTO) {

    return codeDetailService.create(createDTO);
  }

  @PatchMapping("/code-detail")
  public ApiResponse<Long> update(CodeDetailUpdateDTO updateDTO) {

    return codeDetailService.update(updateDTO);
  }
}
