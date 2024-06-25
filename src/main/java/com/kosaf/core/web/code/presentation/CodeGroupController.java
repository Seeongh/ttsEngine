package com.kosaf.core.web.code.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kosaf.core.api.code.application.dto.group.CodeGroupDetailDTO;

@Controller
@RequestMapping("/code/group")
public class CodeGroupController {

  /**
   * 코드 그룹 목록
   * @return String page
   */
  @RequestMapping("/list")
  public String list() {

    return "/code/group/codeGroupList";
  }

  /**
   * 코드 그룹 상세
   * @param CodeGroupDetailDTO
   * @return String page
   */
  @RequestMapping("/info")
  public String info(CodeGroupDetailDTO codeGroupInfoDTO, Model model) {

    model.addAttribute("detailDTO", codeGroupInfoDTO);

    return "/code/group/codeGroupInfo";
  }

  /**
   * 코드 그룹 등록
   * @return String page
   */
  @RequestMapping("/regist")
  public String regist() {

    return "/code/group/codeGroupRegist";
  }

  /**
   * 코드 그룹 수정
   * @param CodeGroupDetailDTO
   * @return String page
   */
  @RequestMapping("/edit")
  public String update(CodeGroupDetailDTO codeGroupInfoDTO, Model model) {

    model.addAttribute("detailDTO", codeGroupInfoDTO);

    return "/code/group/codeGroupEdit";
  }

}
