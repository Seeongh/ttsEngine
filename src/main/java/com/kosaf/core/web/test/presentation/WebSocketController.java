package com.kosaf.core.web.test.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSocketController {

  @GetMapping("/websocket")
  public String websocket() {
    return "/test/websocket";
  }
}
