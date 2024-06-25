package com.kosaf.core.web.error.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

  // View를 위한 WebController

  // private static final Logger log = LogManager.getLogger(ErrorController.class);

  @GetMapping("/403")
  public String forbidden() {

    return "/error/403";
    // return "/error/403.core";
  }

}
