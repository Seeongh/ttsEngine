package com.kosaf.core.api.menu.application;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.menu.application.dto.MenuCreateDTO;
import com.kosaf.core.api.menu.application.dto.MenuListDTO;
import com.kosaf.core.api.menu.application.dto.MenuUpdateDTO;
import com.kosaf.core.api.menu.application.dto.RoleListDTO;
import com.kosaf.core.api.menu.application.dto.RoleListParam;
import com.kosaf.core.api.menu.application.dto.RoleMenusCreateDTO;
import com.kosaf.core.api.menu.domain.Menu;
import com.kosaf.core.api.menu.domain.RoleMenu;
import com.kosaf.core.api.menu.infrastructure.MenuMapper;
import com.kosaf.core.common.ApiResponse;
import com.kosaf.core.common.PageResult;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MenuService {

  private final MenuMapper menuMapper;

  // 부모 메뉴
  public ApiResponse<List<MenuListDTO>> findByRoot() {

    try {

      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      Map<String, Object> paramMap = new HashMap<>();
      Integer userSysId = loginUser.getSysId();

      paramMap.put("userSysId", userSysId);
      paramMap.put("parentMenuNo", 0);

      List<MenuListDTO> list = Optional
          .ofNullable(menuMapper.findByRoot(paramMap))
          .orElseGet(Collections::emptyList) // null 대신 빈 리스트 반환
          .stream()
          .map(MenuListDTO::of)
          .collect(Collectors.toList()); // 목록 조회 및 DTO 변환
      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);
    } catch (Exception e) {

      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  // 하위 메뉴
  public ApiResponse<List<MenuListDTO>> findBySubMenu(Long parentMenuNo) {

    try {

      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      Integer userSysId = loginUser.getSysId();

      Map<String, Object> paramMap = new HashMap<>();

      paramMap.put("userSysId", userSysId);
      paramMap.put("parentMenuNo", parentMenuNo);

      List<MenuListDTO> list = Optional
          .ofNullable(menuMapper.findBySubMenu(paramMap))
          .orElseGet(Collections::emptyList) // null 대신 빈 리스트 반환
          .stream()
          .map(MenuListDTO::of)
          .collect(Collectors.toList()); // 목록 조회 및 DTO 변환

      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {

      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  // 트리메뉴목록
  public ApiResponse<List<MenuListDTO>> findByTree() {

    try {

      List<MenuListDTO> list = Optional
          .ofNullable(menuMapper.findByTree())
          .orElseGet(Collections::emptyList)
          .stream()
          .map(MenuListDTO::of)
          .collect(Collectors.toList());

      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {

      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  // 메뉴 생성
  @Transactional
  public ApiResponse<Long> createMenu(MenuCreateDTO createDTO) {

    if (createDTO == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }
    try {
      // 작성자 sysId
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();

      if (loginUser == null || loginUser.getSysId() == null) {
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);
      }
      if (createDTO.getMenuNo() == null) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
      }

      Menu menu = createDTO.toEntity();

      menu.create(loginUser.getSysId());

      long result = menuMapper.create(menu);

      return result > 0 ? ApiResponse.of(HttpStatus.CREATED, result)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  // 메뉴 수정
  @Transactional
  public ApiResponse<Long> updateMenu(MenuUpdateDTO updateDTO) {

    if (updateDTO == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
      if (loginUser == null || loginUser.getSysId() == null) {
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);
      }
      if (updateDTO.getMenuNo() == null) {
        return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
      }
      Menu menu = updateDTO.toEntity();
      menu.update(loginUser.getSysId());
      Long result = menuMapper.update(menu);

      return result > 0 ? ApiResponse.of(HttpStatus.OK, result)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  @Transactional
  public ApiResponse<Void> deleteMenu(Long menuNo) {

    // 삭제할 메뉴가 있는지 확인
    ApiResponse<Menu> menuResponse = menuInfo(menuNo);

    if (menuResponse.getStatus() != HttpStatus.OK.value()) {
      return ApiResponse.of(HttpStatus.valueOf(menuResponse.getStatus()), null);
    }

    try {
      // 삭제
      Long result = menuMapper.delete(menuNo);

      return result > 0 ? ApiResponse.of(HttpStatus.NO_CONTENT, null)
          : ApiResponse.of(HttpStatus.NOT_FOUND, null);
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

  public ApiResponse<Menu> menuInfo(Long menuNo) {

    try {
      Optional<Menu> menuInfo = Optional.ofNullable(menuMapper.findByMenu(menuNo));
      return menuInfo
          .map(menu -> ApiResponse.of(HttpStatus.OK, menu))
          .orElseGet(() -> ApiResponse.of(HttpStatus.NOT_FOUND, null));
    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  public ApiResponse<PageResult<RoleListDTO>> findByRoleAll(RoleListParam param) {

    try {

      PageResult<RoleListDTO> list = PageResult
          .<RoleListDTO>builder()
          .page(param.getPage())
          .pageScale(param.getPageScale())
          .totalCount(menuMapper.findByRoleAllCount(param))
          .items(Optional
              .ofNullable(menuMapper.findByRoleAll(param))
              .orElseGet(Collections::emptyList) // null 대신 빈 리스트 반환
              .stream()
              .collect(Collectors.toList())) // 목록 조회 및 DTO 변환
          .build();
      return list.getItems().isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  public ApiResponse<List<MenuListDTO>> findByRoleId(String roleId) {

    try {

      List<MenuListDTO> list = Optional
          .ofNullable(menuMapper.findByRoleId(roleId))
          .orElseGet(Collections::emptyList) // null 대신 빈 리스트 반환
          .stream()
          .map(MenuListDTO::of)
          .collect(Collectors.toList()); // 목록 조회 및 DTO 변환

      return list.isEmpty() ? ApiResponse.of(HttpStatus.NOT_FOUND, null)
          : ApiResponse.of(HttpStatus.OK, list);

    } catch (Exception e) {

      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

  }

  // 메뉴 생성
  @Transactional
  public ApiResponse<Long> createRoleByMenus(List<RoleMenusCreateDTO> params) {

    if (params == null) {
      return ApiResponse.of(HttpStatus.BAD_REQUEST, null);
    }

    try {
      // 작성자 sysId
      CustomUserDetails loginUser =
          (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
      if (loginUser == null || loginUser.getSysId() == null) {
        return ApiResponse.of(HttpStatus.UNAUTHORIZED, null);
      }

      String roleId = params.get(0).getRoleId();

      // insert이전에 delete
      menuMapper.deleteRoleByMenu(roleId);

      long resultCnt = 0;
      // 체크박스 체크한 목록으로 insert
      for (RoleMenusCreateDTO roleMenusCreateDTO : params) {

        RoleMenu roleMenu = roleMenusCreateDTO.toEntity();
        roleMenu.create(loginUser.getSysId());

        long count = menuMapper.createRoleByMenus(roleMenu);
        resultCnt = count + resultCnt;

      }

      return resultCnt > 0 ? ApiResponse.of(HttpStatus.CREATED, resultCnt)
          : ApiResponse.of(HttpStatus.UNPROCESSABLE_ENTITY, null);

    } catch (Exception e) {
      return ApiResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
  }

}
