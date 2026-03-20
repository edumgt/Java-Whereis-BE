package com.ssafy.live.user.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
		if (HttpMethod.OPTIONS.matches(request.getMethod())) {
			return true;
		}

		final String token = request.getHeader("access-token");

		if (token == null) {
			log.debug("헤더에 access-token 정보가 없삼");
			response.getWriter().append("not Login");
			return false;
		}

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
