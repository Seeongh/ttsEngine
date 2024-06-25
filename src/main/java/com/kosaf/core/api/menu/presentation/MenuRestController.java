package com.kosaf.core.api.menu.presentation;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kosaf.core.api.menu.application.MenuService;
import com.kosaf.core.api.menu.application.dto.MenuCreateDTO;
import com.kosaf.core.api.menu.application.dto.MenuListDTO;
import com.kosaf.core.api.menu.application.dto.MenuUpdateDTO;
import com.kosaf.core.api.menu.application.dto.RoleListDTO;
import com.kosaf.core.api.menu.application.dto.RoleListParam;
import com.kosaf.core.api.menu.application.dto.RoleMenusCreateDTO;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MenuRestController {

  private final MenuService menuService;
  private static final Logger log = LoggerFactory.getLogger(MenuRestController.class);

  // header 영역 메뉴 목록
  @GetMapping("/parent-menu")
  public ApiResponse<List<MenuListDTO>> findByRoot() {

    return menuService.findByRoot();
  }

  // Left 영역 메뉴 목록
  @GetMapping("/sub-menu/{parentMenuNo}")
  public ApiResponse<List<MenuListDTO>> findBySubMenu(@PathVariable Long parentMenuNo) {

    return menuService.findBySubMenu(parentMenuNo);
  }

  // 메뉴관리 tree 목록
  @GetMapping("/tree-menus")
  public ApiResponse<List<MenuListDTO>> findByTree() {

    return menuService.findByTree();
  }

  // 메뉴 create
  @PostMapping("/menu")
  public ApiResponse<Long> createMenu(@RequestBody MenuCreateDTO createDTO) {

    return menuService.createMenu(createDTO);
  }

  // 메뉴 update
  @PatchMapping("/menu")
  public ApiResponse<Long> updateMenu(@RequestBody MenuUpdateDTO updateDTO) {

    return menuService.updateMenu(updateDTO);
  }

  // 메뉴 delete
  @DeleteMapping("/menu/{menuNo}")
  public ApiResponse<Void> deleteMenu(@PathVariable Long menuNo) {

    return menuService.deleteMenu(menuNo);
  }

  // 메뉴설정 목록
  @GetMapping("/menu-config")
  public ApiResponse<PageResult<RoleListDTO>> findByRoleAll(RoleListParam param) {

    return menuService.findByRoleAll(param);
  }

  // 팝업 트리메뉴 - 등록된 menuNo
  @GetMapping("/check-menus/{roleId}")
  public ApiResponse<List<MenuListDTO>> findByRoleId(@PathVariable String roleId) {

    return menuService.findByRoleId(roleId);
  }

  // 권한별 메뉴 생성
  @PostMapping("/role-menus")
  public ApiResponse<Long> createRoleByMenus(@RequestBody List<RoleMenusCreateDTO> params) {

    return menuService.createRoleByMenus(params);
  }

}
