package com.kosaf.core.config.security.authorization;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.hierarchicalroles.NullRoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * url 접근 요청시, 요청에 대한 권한을 결정하는 AuthorizatioinManager의 구현체.<br>
 * DB로부터 조회해온 자원,권한(롤) 정보, 롤 계층 정보를 접근자의 인증정보를 비교하여 요청에 대한 권한을 결정한다.
 */
public class UrlAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

  private LinkedHashMap<RequestMatcher, List<String>> requestMap = new LinkedHashMap<>();
  private RoleHierarchy roleHierarchy = new NullRoleHierarchy();

  public UrlAuthorizationManager(LinkedHashMap<RequestMatcher, List<String>> requestMap) {
    this.requestMap = requestMap;
  }

  public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
    this.roleHierarchy = roleHierarchy;
  }

  @Override
  public AuthorizationDecision check(Supplier<Authentication> supplier,
      RequestAuthorizationContext object) {

    Authentication authentication = supplier.get();
    HttpServletRequest request = object.getRequest();

    if (requestMap != null) {

      for (Map.Entry<RequestMatcher, List<String>> entry : requestMap.entrySet()) {
        RequestMatcher requestMatcher = entry.getKey();
        
        if (requestMatcher.matches(request)) {
          Iterator<String> iterator = entry.getValue().iterator();

          while (iterator.hasNext()) {
            String authority = iterator.next();
            boolean isGranted = isGranted(authentication, authority);

            if (isGranted || (!isGranted && !iterator.hasNext())) {
              return new AuthorityAuthorizationDecision(isGranted,
                  AuthorityUtils.createAuthorityList(authority));
            }
          }
        }
      }
    }
    return new AuthorizationDecision(true);
  }

  private boolean isGranted(Authentication authentication, String authority) {

    if (authentication == null || authority == null || authority.isEmpty()) {
      return false;
    }

    for (GrantedAuthority grantedAuthority : getGrantedAuthorities(authentication)) {
      if (authority.equals(grantedAuthority.getAuthority())) {
        return true;
      }
    }

    return false;
  }

  private Collection<? extends GrantedAuthority> getGrantedAuthorities(
      Authentication authentication) {
    return this.roleHierarchy.getReachableGrantedAuthorities(authentication.getAuthorities());
  }

}
