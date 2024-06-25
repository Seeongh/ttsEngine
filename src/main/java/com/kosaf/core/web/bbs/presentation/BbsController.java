package com.kosaf.core.web.bbs.presentation;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;

@Controller
@RequestMapping("/bbs")
public class BbsController {

  // View를 위한 WebController

  @GetMapping("/list")
  public String list(Model model) {

    // 로그인
    CustomUserDetails loginUser =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

    model.addAttribute("loginUser", loginUser);

    return "/bbs/bbsList";
  }

  @GetMapping("/info")
  public String info(Long bbsSeq, Model model) {

    // 로그인
    CustomUserDetails loginUser =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

    model.addAttribute("loginUser", loginUser);
    model.addAttribute("bbsSeq", bbsSeq);

    return "/bbs/bbsInfo";
  }

  @GetMapping("/regist")
  public String regist(Long bbsSeq, Model model) {

    return "/bbs/bbsRegist";
  }

  @GetMapping("/edit")
  public String edit(Long bbsSeq, Model model) {

    // 로그인
    CustomUserDetails loginUser =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
    model.addAttribute("loginUser", loginUser);

    model.addAttribute("bbsSeq", bbsSeq);

    return "/bbs/bbsEdit";
  }
}
