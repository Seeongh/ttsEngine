package com.kosaf.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import com.kosaf.core.config.security.application.CustomUserDetailService;
import com.kosaf.core.config.security.application.SecurityResourceService;
import com.kosaf.core.config.security.authentication.handler.CustomAuthenticationFailureHandler;
import com.kosaf.core.config.security.authentication.handler.CustomAuthenticationSuccessHandler;
import com.kosaf.core.config.security.authentication.handler.CustomLogoutSuccessHandler;
import com.kosaf.core.config.security.authentication.provider.CustomUserAuthenticationProvider;
import com.kosaf.core.config.security.authorization.SecurityResourcesMapFactoryBean;
import com.kosaf.core.config.security.authorization.UrlAuthorizationManager;
import com.kosaf.core.config.security.authorization.handler.CustomAccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private PasswordEncoder passwordEncoder;
  private CustomUserDetailService userDetailService;
  @Autowired
  private CustomAuthenticationFailureHandler loginFailureHandler;
  @Autowired
  private CustomAuthenticationSuccessHandler loginSuccessHandler;
  @Autowired
  private CustomLogoutSuccessHandler logoutSuccessHandler;
  @Autowired
  private SecurityResourceService securityResourceService;
  @Autowired
  private CustomAccessDeniedHandler accessDeniedHandler;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(csrf -> csrf.disable());

    http
        .authorizeHttpRequests(authorize -> authorize
            .antMatchers("/error", "/error/**","/","/replace/**","/ttsDemo/**", "/frequency/**","/monitoring/**")
            .permitAll()
            .requestMatchers(new AntPathRequestMatcher("/**"))
            .access(urlAuthorizationManager())
            .anyRequest()
            .authenticated());

    http
        .formLogin(login -> login
            .loginPage("/login")
            .loginProcessingUrl("/api/login")
            .usernameParameter("loginId")
            .passwordParameter("pswdEncpt")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .permitAll())
        .logout(logout -> logout
            .logoutUrl("/api/logout")
            .logoutSuccessUrl("/login")
            .logoutSuccessHandler(logoutSuccessHandler))
        .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler));

    return http.build();
  }

  @Bean
  AuthenticationManager authenticationManager() {
    CustomUserAuthenticationProvider provider =
        new CustomUserAuthenticationProvider(userDetailService, passwordEncoder);
    return new ProviderManager(provider);
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  UrlAuthorizationManager urlAuthorizationManager() {
    UrlAuthorizationManager urlAuthorizationManager =
            new UrlAuthorizationManager(securityResourcesMapFactoryBean().getObject());
    urlAuthorizationManager.setRoleHierarchy(roleHierarchy());
    return urlAuthorizationManager;
  }

  private SecurityResourcesMapFactoryBean securityResourcesMapFactoryBean() {
    SecurityResourcesMapFactoryBean securityResourcesMapFactoryBean =
        new SecurityResourcesMapFactoryBean();
    securityResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
    return securityResourcesMapFactoryBean;
  }

  @Bean
  RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy(securityResourceService.getRoleHierarchy());
    return hierarchy;
  }
}
