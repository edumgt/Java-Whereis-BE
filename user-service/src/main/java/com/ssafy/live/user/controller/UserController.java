package com.ssafy.live.user.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.live.user.dto.User;
import com.ssafy.live.user.model.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	static final int EXPIRED_MINUTES = 10;
	static final String SECRET_KEY = "ssafy";

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> map, HttpServletResponse response)
			throws Exception {
		User userInfo = userService.login(map);
		if (userInfo != null) {
			String token = Jwts.builder()
					.setHeaderParam("typ", "JWT").setHeaderParam("alg", "HS256")
					.claim("id", userInfo.getUserId()).claim("pwd", userInfo.getUserPwd())
					.claim("manager", userInfo.getManager())
					.setExpiration(new Date(System.currentTimeMillis() + 3600L * 60 * EXPIRED_MINUTES))
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes("UTF-8")).compact();
			log.debug("발급된 토큰 : {}", token);
			Map<String, String> result = new HashMap<>();
			result.put("token", token);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody User user) throws Exception {
		try {
			int cnt = userService.regist(user);
			if (cnt == 1) {
				return new ResponseEntity<>("success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/find-id")
	public ResponseEntity<?> findId(@RequestBody User user) throws Exception {
		try {
			String userId = userService.findId(user);
			if (userId != null) {
				return new ResponseEntity<>(userId, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/find-pwd")
	public ResponseEntity<?> findPwd(@RequestBody User user) throws Exception {
		try {
			String userPwd = userService.findPwd(user);
			if (userPwd != null) {
				return new ResponseEntity<>(userPwd, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/confirm/{id}")
	public ResponseEntity<?> userConfirm(@PathVariable String id) throws Exception {
		try {
			User getUser = userService.confirm(id);
			return new ResponseEntity<>(getUser, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/update")
	public ResponseEntity<?> modify(@RequestBody User user) {
		try {
			if (user.getUserName() != null && user.getUserName().equals("")) user.setUserName(null);
			if (user.getUserPwd() != null && user.getUserPwd().equals("")) user.setUserPwd(null);
			if (user.getEmailId() != null && user.getEmailId().equals("")) user.setEmailId(null);
			if (user.getEmailDomain() != null && user.getEmailDomain().equals("선택")) user.setEmailDomain(null);
			int cnt = userService.update(user);
			if (cnt == 1) {
				return new ResponseEntity<>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			userService.delete(id);
			return new ResponseEntity<>("success", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpSession session) {
		session.invalidate();
		return new ResponseEntity<>("success", HttpStatus.OK);
	}
}
