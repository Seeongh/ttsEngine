package com.kosaf.core.api.code.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.code.application.CodeGroupService;
import com.kosaf.core.api.code.application.dto.group.CodeGroupCreateDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupDetailDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupListDTO;
import com.kosaf.core.api.code.application.dto.group.CodeGroupListParam;
import com.kosaf.core.api.code.application.dto.group.CodeGroupUpdateDTO;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CodeGroupRestController {

  private final CodeGroupService codeService;

  @GetMapping("/code-group")
  public ApiResponse<PageResult<CodeGroupListDTO>> findAll(CodeGroupListParam param) {

    return codeService.findAll(param);
  }

  @GetMapping("/code-group/{groupCd}")
  public ApiResponse<CodeGroupDetailDTO> findByGroupCd(@PathVariable String groupCd){

    return codeService.findByGroupCd(groupCd);
  }

  @PostMapping("/code-group")
  public ApiResponse<Long> create(CodeGroupCreateDTO createDto) {

    return codeService.create(createDto);
  }

  @PatchMapping("/code-group")
  public ApiResponse<CodeGroupUpdateDTO> update(CodeGroupUpdateDTO updateDTO) {

    return codeService.update(updateDTO);
  }
}
