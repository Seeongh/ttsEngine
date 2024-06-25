package com.kosaf.core.api.bbs.presentation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.bbs.application.BbsService;
import com.kosaf.core.api.bbs.application.dto.BbsCreateDTO;
import com.kosaf.core.api.bbs.application.dto.BbsDTO;
import com.kosaf.core.api.bbs.application.dto.BbsDetailDTO;
import com.kosaf.core.api.bbs.application.dto.BbsListParam;
import com.kosaf.core.api.bbs.application.dto.BbsUpdateDTO;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.validation.ValidSequence;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BbsRestController {

  // RestAPI Controller

  private final BbsService bbsService;

  @GetMapping("/bbss")
  public ApiResponse<PageResult<BbsDTO>> findAll(BbsListParam param) {

    return bbsService.findAll(param);
  }

  @GetMapping("/bbs/excel-down")
  public void excelDown(BbsListParam param, HttpServletRequest req, HttpServletResponse res) {

    bbsService.excelDown(param, req, res);
  }

  @GetMapping("/bbs/{bbsSeq}")
  public ApiResponse<BbsDetailDTO> findById(
      @PathVariable @Min(value = 0, message = "요청이 올바르지 않습니다.") Long bbsSeq) {

    return bbsService.findById(bbsSeq);
  }

  @PostMapping("/bbs")
  public ApiResponse<Long> create(
      @RequestBody @Validated(ValidSequence.class) BbsCreateDTO createDTO) {

    return bbsService.create(createDTO);
  }

  @PatchMapping("/bbs/{bbsSeq}/views")
  public ApiResponse<Void> updateInqCnt(
      @PathVariable @Min(value = 0, message = "요청이 올바르지 않습니다.") Long bbsSeq) {

    return bbsService.updateInqCnt(bbsSeq);
  }

  @PatchMapping("/bbs")
  public ApiResponse<Long> update(
      @RequestBody @Validated(ValidSequence.class) BbsUpdateDTO updateDTO) {

    return bbsService.update(updateDTO);
  }

  @DeleteMapping("/bbs/{bbsSeq}")
  public ApiResponse<Void> delete(
      @PathVariable @Min(value = 0, message = "요청이 올바르지 않습니다.") Long bbsSeq) {

    return bbsService.delete(bbsSeq);
  }
}
