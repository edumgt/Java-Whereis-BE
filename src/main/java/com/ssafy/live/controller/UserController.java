package com.ssafy.live.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
//TODO: 내일 "서영이가" 할 것
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ssafy.live.dto.User;
import com.ssafy.live.model.service.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/users")
public class UserController {

	private final UserService userService;
	final static int EXPRIED_MINUTES = 10;
	final static String SECRET_KEY = "ssafy";

	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	// 로그인
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> map, Model model, HttpSession session,
			HttpServletResponse response) throws Exception {
		
		// 유저 정보 조회
		System.out.println("들어옴");
		System.out.println(map);
		User userInfo = userService.login(map);

		System.out.println(userInfo.toString());
		// 로그인 성공
		if (userInfo != null) {

			// 토큰 만들엉
			String token = Jwts.builder()
					// header
					.setHeaderParam("typ", "JWT").setHeaderParam("alg", "HS256")
					// payload
					.claim("id", userInfo.getUserId()).claim("pwd", userInfo.getUserPwd())
					.claim("manager", userInfo.getManager())
					.setExpiration(new Date(System.currentTimeMillis() + 3600 * 60 * EXPRIED_MINUTES))
					// signature
					.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes("UTF-8")).compact();

			log.debug("발급된 토큰 : {}", token);
			Map<String, String> result = new HashMap<>();
			result.put("token", token);

			System.out.println(token);
			return new ResponseEntity<Map<String, String>>(result, HttpStatus.OK);

			/*
			 * { "token" : "132143243어쩌구" }
			 */

		}
		// 로그인 실패
		else
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 회원가입
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody User user) throws Exception {

		try {
			System.out.println(user.toString());
			int cnt = userService.regist(user); // DB에 접근해 회원가입 진행

			System.out.println(cnt);

			if (cnt == 1) { // 회원가입 정보가 있다면
				return new ResponseEntity<String>("success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 회원 아이디 찾기
	@PostMapping("/find-id")
	public ResponseEntity<?> findId(@RequestBody User user) throws Exception {

		try {
			System.out.println(user.toString());
			String userId = userService.findId(user);
			System.out.println(userId);
			if (userId != null) {
//				JSONObject resp = new JSONObject();
//				resp.put("userId", userId);
				return new ResponseEntity<String>(userId, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 회원 비밀번호 찾기
	@PostMapping("/find-pwd")
	public ResponseEntity<?> findPwd(@RequestBody User user) throws Exception {
		try {
			String userPwd = userService.findPwd(user);
			if (userPwd != null) {
				return new ResponseEntity<String>(userPwd, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 회원 조회
	@GetMapping("/confirm/{id}")
	public ResponseEntity<?> userConfirmPage(@PathVariable String id) throws Exception {

		try {
			User getUser = userService.confirm(id);
			return new ResponseEntity<User>(getUser, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 회원 정보 수정
	@PutMapping("/update")
	public ResponseEntity<?> modify(@RequestBody User user) {
		try {
			// 빈칸으로 넣는 경우 값을 null로 처리해야 값이 변경되지 않음
			if (user.getUserName().equals("")) {
				user.setUserName(null);
			}
			if (user.getUserPwd().equals("")) {
				user.setUserPwd(null);
			}
			if (user.getEmailId().equals("")) {
				user.setEmailId(null);
			}
			if (user.getEmailDomain().equals("선택")) {
				user.setEmailDomain(null);
			}

			int cnt = userService.update(user);

			if (cnt == 1) {
				return new ResponseEntity<String>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) {
		try {
			System.out.println(id);
			userService.delete(id);
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "main";
	}
}