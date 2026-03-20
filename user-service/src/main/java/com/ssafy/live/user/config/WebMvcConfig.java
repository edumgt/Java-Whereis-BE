package com.ssafy.live.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.live.user.interceptor.LoginConfirmInterceptor;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.ssafy.live.user.model.mapper"})
public class WebMvcConfig implements WebMvcConfigurer {

	@Autowired
	private LoginConfirmInterceptor interceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(interceptor)
				.addPathPatterns("/users/**")
				.excludePathPatterns("/users/login", "/users/join", "/users/find-id", "/users/find-pwd");
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE");
	}

	@Bean
	public OpenAPI userServiceOpenAPI() {
		return new OpenAPI().info(new Info()
				.title("User Service API")
				.description("WhereisMyHome User Microservice API")
				.version("v1")
				.contact(new Contact().name("SSAFY").url("https://edu.ssafy.com").email("ssafy@ssafy.com")));
	}
}
