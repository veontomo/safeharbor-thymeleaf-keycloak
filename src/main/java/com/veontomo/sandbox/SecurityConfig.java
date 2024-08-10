package com.veontomo.sandbox;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
class SecurityConfig {

	private final LogoutHandler logoutHandler;

	SecurityConfig(LogoutHandler logoutHandler) {
		this.logoutHandler = logoutHandler;
	}

	@Bean
	public SecurityWebFilterChain chain(ServerHttpSecurity http) {
		http.authorizeExchange(
				auth -> auth.pathMatchers("/", "/css/bootstrap.min.css").permitAll().anyExchange().authenticated());
		http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
		http.oauth2Login(Customizer.withDefaults()).logout(logout -> logout.logoutHandler(logoutHandler)
				.logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler()));
		return http.build();
	}

}
