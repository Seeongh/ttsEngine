package com.kosaf.core.api.user.infrastructure;

import org.apache.ibatis.annotations.Mapper;
import com.kosaf.core.api.user.domain.User;
import com.kosaf.core.config.security.application.dto.CustomUserDetails;

@Mapper
public interface UserMapper {

  public User findByLoginId(String loginId);

  public CustomUserDetails userAndRoleByLoginId(String loginId);
}
