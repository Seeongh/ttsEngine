package com.kosaf.core.config.security.application;

import java.util.Optional;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.kosaf.core.api.user.infrastructure.UserMapper;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;

/**
 * spring security 인증을 위한 UserDetailsService 구현체.
 */
@Component
public class CustomUserDetailService implements UserDetailsService {

  private final UserMapper userMapper;

  public CustomUserDetailService(UserMapper userMapper) {
    this.userMapper = userMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

    // TODO message properties 적용
    CustomUserDetails user = Optional
        .ofNullable(userMapper.userAndRoleByLoginId(loginId))
        .orElseThrow(() -> new UsernameNotFoundException("loginId not found"));

    if (user.getRoles() == null || user.getRoles().isEmpty()) {
      throw new AuthenticationCredentialsNotFoundException("권한이 등록되지 않은 사용자입니다.");
    }

    return user;
  }
}
