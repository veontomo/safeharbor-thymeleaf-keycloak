package com.veontomo.sandbox;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class LogoutSuccessHandler implements ServerLogoutSuccessHandler {

	@Override
	public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
		return exchange.getExchange().getSession().flatMap(session -> {
			session.invalidate();
			return Mono.fromRunnable(() -> {
				ServerWebExchange webExchange = exchange.getExchange();
				webExchange.getResponse().setStatusCode(HttpStatus.FOUND);
				webExchange.getResponse().getHeaders().setLocation(URI.create("/"));
			});
		});
	}

}
