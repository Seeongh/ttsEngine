package com.kosaf.core.config;

// @Configuration
// @EnableWebSecurity
public class SecurityConfig {

  // @Bean
  // SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
  // http
  // .authorizeRequests(authorize -> authorize
  // .antMatchers("/css/**", "/js/**", "/images/**")
  // .permitAll()
  // .anyRequest()
  // .authenticated())
  // .formLogin(
  // formLogin -> formLogin.loginPage("/login").permitAll().failureUrl("/login?error=true"))
  // .logout(logout -> logout.logoutSuccessUrl("/login?logout=true"));
  // return http.build();
  // }
  //
  // @Bean
  // InMemoryUserDetailsManager userDetailsService() {
  // PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  // UserDetails user =
  // User.builder().username("user").password(encoder.encode("password")).roles("USER").build();
  //
  // return new InMemoryUserDetailsManager(user);
  // }

}
