package com.ssafy.live.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ssafy.live.dto.User;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LoginConfirmInterceptor implements HandlerInterceptor {
	final static String SECRET_KEY = "ssafy";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.debug("요청 메소드 종류 : {}", request.getMethod());
		// OPTIONS 메소드에서 넘어오는 요청 처리해줌
		if(HttpMethod.OPTIONS.matches(request.getMethod())) {
			return true;
		}
		
		final String token = request.getHeader("access-token");
		
		// 토큰 존재 여부 체크
		if(token == null) {
			log.debug("헤더에 access-token 정보가 없삼");
			response.getWriter().append("not Login");
			return false;
		}
		
		//토큰 유효성 체크
		try {
			Jwts.parser()
					.setSigningKey(SECRET_KEY.getBytes("UTF-8"))
					.parseClaimsJws(token);
			
			log.debug("토큰 존재하고 유효함 요청 정상 처리 ^,^");
			return true;
			
		} catch (Exception e) {
			log.debug("토큰은 존재하지만 유효하지 않음! : {}", e.getMessage());
			response.getWriter().append("not Valid");
			return false;
		}
	}
}