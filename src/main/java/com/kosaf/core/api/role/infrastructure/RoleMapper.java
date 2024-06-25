package com.kosaf.core.api.role.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.role.domain.RoleHierarchy;
import com.kosaf.core.config.security.application.dto.SecurityResourceDTO;

@Mapper
public interface RoleMapper {

  /**
   * DB에 등록된 모든 리소스와 해당 리소스에 접근 가능한 role 목록을 조회한다. <br>
   * PUBLIC.COM_ROLE_AUTHOR_REL, PUBLIC.COM_AUTHOR_INFO CAI
   * 
   * @return AntPattern의 리소스와 role List를 가지는 SecurityResourceDTO 목록
   */
  public List<SecurityResourceDTO> findAllSecurityResource();

  /**
   * DB에 등록된 모든 role 계층 정보를 조회한다.<br>
   * PUBLIC.COM_ROLE_HIERARCHY_INFO
   * 
   * @return 부모 롤 id, 자식 롤 id를 가지는 RoleHierarchy 목록
   */
  public List<RoleHierarchy> findAllRoleHierarchy();
}
