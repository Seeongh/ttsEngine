package com.kosaf.core.web.menu.presentation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/menu")
public class MenuController {

  private static final Logger log = LogManager.getLogger(MenuController.class);

  @GetMapping("/menuList")
  public String list() {

    return "/menu/menuList";
  }

  @GetMapping("/menuConfig")
  public String menuConfigList() {

    return "/menu/menuConfig";
  }

}
