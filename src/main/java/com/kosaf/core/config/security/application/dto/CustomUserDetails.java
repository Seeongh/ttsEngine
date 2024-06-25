package com.kosaf.core.config.security.application.dto;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.kosaf.core.api.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {

  // 로그인 인증을 위한 DTO
  // Spring Security가 제공하는 UserDetails의 구현체
  private static final long serialVersionUID = 1L;

  private String loginId;
  private Integer sysId;
  private String userNm;
  private String pswdEncpt;
  private String roles;

  public User toEntity() {
    return User
        .builder()
        .loginId(this.loginId)
        .sysId(this.sysId)
        .userNm(this.userNm)
        .pswdEncpt(this.pswdEncpt)
        .build();
  }

  public static CustomUserDetails of(User user) {
    return CustomUserDetails
        .builder()
        .loginId(user.getLoginId())
        .sysId(user.getSysId())
        .userNm(user.getUserNm())
        .pswdEncpt(user.getPswdEncpt())
        .build();
  }

  public void erasePassword() {
    this.pswdEncpt = null;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    if (this.roles.isEmpty()) {
      return null;
    } else {
      return Arrays
          .stream(this.roles.split(","))
          .map(SimpleGrantedAuthority::new)
          .collect(Collectors.toList());
    }
  }

  @Override
  public String getPassword() {
    return this.pswdEncpt;
  }

  @Override
  public String getUsername() {
    return this.loginId;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
