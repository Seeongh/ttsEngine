package com.kosaf.core.config.security.authentication.provider;

import java.util.Collection;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.kosaf.core.config.security.application.CustomUserDetailService;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;

/**
 * Spring Security Login을 처리하는 AuthenticationProvider 구현체.
 */
@Component
public class CustomUserAuthenticationProvider implements AuthenticationProvider {

  private final CustomUserDetailService userDetailService;
  private final PasswordEncoder passwordEncoder;

  public CustomUserAuthenticationProvider(CustomUserDetailService userDetailService,
      PasswordEncoder passwordEncoder) {
    this.userDetailService = userDetailService;
    this.passwordEncoder = passwordEncoder;
  }

  /**
   * 인증 처리 메소드. <br>
   * SecurityConfig.java filterChain에서 설정한 loginProcessingUrl 진입 시 수행한다.
   */
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    UsernamePasswordAuthenticationToken authToken =
        (UsernamePasswordAuthenticationToken) authentication;

    String username = authToken.getName();
    String password = (String) authToken.getCredentials();
    if (username == null || username.isEmpty()) {
      throw new UsernameNotFoundException("ID를 입력해주세요."); // TODO message properties 적용
    }
    if (password == null || password.isEmpty()) {
      throw new UsernameNotFoundException("비밀번호를 입력해주세요."); // TODO message properties 적용
    }

    // DB에 등록된 사용자 정보 조회
    CustomUserDetails userDetails =
        (CustomUserDetails) userDetailService.loadUserByUsername(username);

    // pw 인증
    String pswdEncpt = userDetails.getPswdEncpt();
    verifyCredentials(password, pswdEncpt);

    Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

    authToken = UsernamePasswordAuthenticationToken
        .authenticated(userDetails.getSysId(), pswdEncpt, authorities);

    // 사용자 정보 response를 위한 user 객체 set
    userDetails.erasePassword();
    authToken.setDetails(userDetails);

    return authToken;
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.equals(authentication);
  }

  private void verifyCredentials(String credentials, String password) {
    if (!passwordEncoder.matches(credentials, password)) {
      throw new BadCredentialsException("Invalid User password"); // TODO message properties 적용
    }
  }

}
