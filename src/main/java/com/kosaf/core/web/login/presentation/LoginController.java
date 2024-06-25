package com.kosaf.core.web.login.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

  // View를 위한 WebController

  @GetMapping("/login")
  public String login() {

    return "/login/login.core";
  }

  @GetMapping("/test/system")
  public String system() {

    return "/login/test/system";
  }

  @GetMapping("/test/admin")
  public String admin() {

    return "/login/test/admin";
  }

  @GetMapping("/test/user")
  public String user() {

    return "/login/test/user";
  }

}
