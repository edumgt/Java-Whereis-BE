package com.ssafy.live.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ssafy.live.interceptor.LoginConfirmInterceptor;

@Configuration
@EnableAspectJAutoProxy // auto-proxy 설정
@MapperScan(basePackages = {"com.ssafy.*.model.mapper"}) // mapper 인터페이스 스캔
public class WebMvcConfig implements WebMvcConfigurer{
    
    // 인터셉터 등록
    @Autowired
    private LoginConfirmInterceptor interceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(interceptor)
    	.addPathPatterns("/users/**","/board/**","/apts/**","/dong-code/**","/admin/**")
    	.excludePathPatterns("/users/login","/users/join","/users/find-id","/users/find-pwd");
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
    // 정적 자원 경로 매핑
    // /assets로 시작하는 요청 시
    // src/main/webapp/resources/assets로 매핑 시켜줌
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**")
        .addResourceLocations("/resources/assets/");
    }
}