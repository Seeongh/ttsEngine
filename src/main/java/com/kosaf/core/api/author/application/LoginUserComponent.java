package com.kosaf.core.api.author.application;

import com.kosaf.core.api.author.infrastructure.AuthMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserComponent {

    private final AuthMapper authMapper;

    public Long authentication(String userId) {
        return authMapper.findById(userId);
    }
}
