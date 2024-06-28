package com.kosaf.core.config.interceptor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@RequiredArgsConstructor
@Configuration
@Slf4j
public class AuthInterceptor  implements WebMvcConfigurer {

    private final UserAuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("언제호출? AuthInterceptor");
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns( "/_resource/**"); //정적 리소스 제외
    }
}