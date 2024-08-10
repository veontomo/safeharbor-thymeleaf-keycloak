package com.veontomo.sandbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import reactor.core.publisher.Mono;

@Component
public class LogoutHandler implements ServerLogoutHandler {

	private static final Logger logger = LoggerFactory.getLogger(LogoutHandler.class);
	private final RestTemplate restTemplate;

	public LogoutHandler(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public Mono<Void> logout(WebFilterExchange exchange, Authentication auth) {
		return logoutFromKeycloak((OidcUser) auth.getPrincipal());
	}

	private Mono<Void> logoutFromKeycloak(OidcUser user) {
		String endSessionEndpoint = user.getIssuer() + "/protocol/openid-connect/logout";
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(endSessionEndpoint)
				.queryParam("id_token_hint", user.getIdToken().getTokenValue());

		ResponseEntity<String> logoutResponse = restTemplate.getForEntity(builder.toUriString(), String.class);
		if (logoutResponse.getStatusCode().is2xxSuccessful()) {
			logger.info("Successfulley logged out from Keycloak");
		} else {
			logger.error("Could not propagate logout to Keycloak");
		}
		return Mono.empty();
	}

}
