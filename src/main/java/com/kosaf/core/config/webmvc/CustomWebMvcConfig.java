package com.kosaf.core.config.webmvc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.kosaf.core.config.interceptor.WebLogInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CustomWebMvcConfig implements WebMvcConfigurer {

  private final WebLogInterceptor webLogInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

    registry.addInterceptor(webLogInterceptor)
      .excludePathPatterns( "/_resource/**",
                            "/api/parent-menu",
                            "/api/sub-menu/**" );
  }

  @Bean
  public ObjectMapper objectMapper() {
      return Jackson2ObjectMapperBuilder
        .json()
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .modules(new JavaTimeModule())
        .build();
  }

}
