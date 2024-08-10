package com.veontomo.sandbox;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class HomeController {

	@GetMapping(path = "/")
	public Mono<String> index() {
		return Mono.just("index");
	}

	@GetMapping(path = "/companies")
	public Mono<String> customers(Mono<Authentication> principal, Model model) {
		Flux<Company> customers = this.fetchData();
		model.addAttribute("companies", customers);
		return principal.doOnNext(p -> {
			model.addAttribute("username", p.getName());
			System.out.println("class: " + p.getClass().getCanonicalName());
		}).thenReturn("companies");
	}


	public Flux<Company> fetchData() {
		final Company item1 = new Company("Tech Innovators Inc.",
				"Artificial Intelligence and Machine Learning Solutions",
				"1234 Silicon Alley, San Francisco, CA 94107, USA");

		final Company item2 = new Company("GreenFuture Energy Ltd.", "Renewable Energy and Sustainable Technologies",
				"5678 Eco Drive, Berlin, 10115, Germany");

		final Company item3 = new Company("HealthNet Solutions", "Healthcare IT and Telemedicine Services",
				"91011 Wellness Avenue, Tokyo, 100-0001, Japan");
		return Flux.fromArray(new Company[] { item1, item2, item3 });
	}
}
