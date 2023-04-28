package com.groupe1.atelier3.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final ServerLogoutHandlerAdapter serverLogoutHandler;
  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
    http
            .authorizeExchange()
            .pathMatchers("/auth/**").permitAll()
            .anyExchange().authenticated()
            .and()
            .csrf(csrf -> csrf.disable())
            .addFilterAt(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .logout()
            .logoutUrl("/auth/logout")
            .logoutHandler(serverLogoutHandler);

    return http.build();
  }
}