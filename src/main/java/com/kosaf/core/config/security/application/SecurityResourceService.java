package com.kosaf.core.config.security.application;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;
import com.kosaf.core.api.role.domain.RoleHierarchy;
import com.kosaf.core.api.role.infrastructure.RoleMapper;
import com.kosaf.core.config.security.application.dto.SecurityResourceDTO;
import lombok.RequiredArgsConstructor;

/**
 * 인가 처리를 위한 권한정보 조회 service
 */
@Service
@RequiredArgsConstructor
public class SecurityResourceService {

  private static final Logger log = LogManager.getLogger(SecurityResourceService.class);

  private final RoleMapper roleMapper;

  /**
   * DB로부터 모든 resource(ant pattern)와 해당 리소스에 접근 가능한 role 목록을 가져온다.
   * 
   * @return <b>LinkedHashMap<k,v></b> <br>
   *         key:requestMatcher <br>
   *         value:해당 request에 접근 가능한 role list
   */
  public LinkedHashMap<RequestMatcher, List<String>> getAllResource() {

    LinkedHashMap<RequestMatcher, List<String>> result = new LinkedHashMap<>();

    try {

      List<SecurityResourceDTO> resourceList = roleMapper.findAllSecurityResource();

      // DB에서 조회한 resource 패턴과 role 정보를 spring security FactoryBean에 등록할 형태로 변환한다.
      resourceList.forEach(role -> {
        result
            .put(new AntPathRequestMatcher(role.getPattern()),
                Stream.of(role.getRoles().split(",")).collect(Collectors.toList()));
      });
      return result;

    } catch (Exception e) {
      log.error("Error : {}", e.getMessage());
      log.error("Cause : {}", e.getCause());
      return result;
    }
  }

  /**
   * DB로부터 롤 계층 정보를 조회하여 spring security 롤 계층정보에 등록 가능한 형태로 반환한다.
   * 
   * @return <b>String</b> 롤 계층 정보 <br>
   *         example : ROLE_A &gt; ROLE_B <br>
   *         ROLE_B &gt; ROLE_AUTHENTICATED <br>
   *         ROLE_AUTHENTICATED &gt; ROLE_UNAUTHENTICATED
   */
  public String getRoleHierarchy() {

    try {

      List<RoleHierarchy> roleHierarchies = roleMapper.findAllRoleHierarchy();

      String roleHierarchy = roleHierarchies
          .stream()
          .map(hierarchy -> hierarchy.getParentRoleId() + " > " + hierarchy.getRoleId())
          .collect(Collectors.joining("\n"));
      return roleHierarchy;

    } catch (Exception e) {
      log.error("Error : {}", e.getMessage());
      log.error("Cause : {}", e.getCause());
      return "";
    }
  }

}
