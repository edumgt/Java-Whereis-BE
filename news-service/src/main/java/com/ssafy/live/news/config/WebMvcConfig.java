package com.ssafy.live.news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE");
	}

	@Bean
	public OpenAPI newsServiceOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("News Service API")
				.description("WhereisMyHome News Microservice API")
				.version("v1")
				.contact(new Contact().name("SSAFY").url("https://edu.ssafy.com").email("ssafy@ssafy.com")));
	}
}
