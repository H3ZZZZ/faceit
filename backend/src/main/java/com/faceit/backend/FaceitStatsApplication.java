package com.faceit.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class FaceitStatsApplication {

	public static void main(String[] args) {
		SpringApplication.run(FaceitStatsApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins(
								"http://localhost:5173",
								"https://6846a520490c3f9985f9d3e0--faceitfrontend.netlify.app",
								"https://frederikhess.dk",
								"https://www.frederikhess.dk"
								"https://api.frederikhess.dk" // add this
						)
						.allowedMethods("*");
			}
		};
	}


}
