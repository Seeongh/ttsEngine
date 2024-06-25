package com.kosaf.core.web.member.presentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
public class MemberController {

  // View를 위한 WebController

  private static final Logger log = LogManager.getLogger(MemberController.class);

  @GetMapping("/list")
  public String list() {

    return "/member/list";
  }

  @GetMapping("/info")
  public String info(String meberId) {

    return "/member/info";
  }

}
