package com.kosaf.core.api.menu.infrastructure;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.menu.application.dto.RoleListDTO;
import com.kosaf.core.api.menu.application.dto.RoleListParam;
import com.kosaf.core.api.menu.domain.Menu;
import com.kosaf.core.api.menu.domain.RoleMenu;

@Mapper
public interface MenuMapper {

  public List<Menu> findByRoot(Map<String, Object> paramMap);

  public List<Menu> findBySubMenu(Map<String, Object> paramMap);

  public List<Menu> findByTree();

  public Long create(Menu menu);

  public Long update(Menu menu);

  public Menu findByMenu(Long menuNo);

  public Long delete(Long menuNo);

  public List<RoleListDTO> findByRoleAll(RoleListParam param);

  public Integer findByRoleAllCount(RoleListParam param);

  public List<Menu> findByRoleId(String roleId);

  public Long createRoleByMenus(RoleMenu roleMenu);

  public Long deleteRoleByMenu(String roleId);
}
