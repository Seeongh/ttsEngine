package com.kosaf.core.config.security.authorization;

import java.util.LinkedHashMap;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import com.kosaf.core.config.security.application.SecurityResourceService;

/**
 * 동적 resource 접근 권한 관리를 위한 FactoryBean <br>
 * DB로부터 resource별 접근 가능 role을 조회하여 Bean으로 생성한다.
 */
@Component
public class SecurityResourcesMapFactoryBean
    implements FactoryBean<LinkedHashMap<RequestMatcher, List<String>>> {

  private SecurityResourceService securityResourceService;
  private LinkedHashMap<RequestMatcher, List<String>> resourceMap;

  public void setSecurityResourceService(SecurityResourceService securityResourceService) {
    this.securityResourceService = securityResourceService;
  }

  private void init() {
    resourceMap = securityResourceService.getAllResource();
  }

  @Override
  public LinkedHashMap<RequestMatcher, List<String>> getObject() {

    if (resourceMap == null) {
      init();
    }

    return resourceMap;
  }

  @Override
  public Class<?> getObjectType() {
    return LinkedHashMap.class;
  }

}
