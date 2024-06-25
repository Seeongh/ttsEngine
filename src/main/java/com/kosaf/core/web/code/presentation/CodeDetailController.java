package com.kosaf.core.web.code.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kosaf.core.api.code.application.dto.detail.CodeDetailDTO;

@Controller
@RequestMapping("/code/detail")
public class CodeDetailController {

  /**
   * 코드 상세 목록
   * @return String page
   */
  @RequestMapping("/list")
  public String list() {

    return "/code/detail/codeDetailList";
  }

  /**
   * 코드 상세 정보
   * @param CodeDetailDTO
   * @return String page
   */
  @RequestMapping("/info")
  public String info(CodeDetailDTO detailDTO, Model model) {

    model.addAttribute("detailDTO", detailDTO);

    return "/code/detail/codeDetailInfo";
  }

  /**
   * 코드 상세 등록
   * @return String page
   */
  @RequestMapping("/regist")
  public String regist() {

    return "/code/detail/codeDetailRegist";
  }

  /**
   * 코드 상세 수정
   * @param CodeDetailDTO
   * @return String page
   */
  @RequestMapping(value = "/edit")
  public String update(CodeDetailDTO detailDTO, Model model) {

    model.addAttribute("detailDTO", detailDTO);

    return "/code/detail/codeDetailEdit";
  }
}
