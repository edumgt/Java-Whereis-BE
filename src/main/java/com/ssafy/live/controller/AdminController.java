package com.ssafy.live.controller;

import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ssafy.live.dto.House;
import com.ssafy.live.dto.User;
import com.ssafy.live.model.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final UserService userService;

	@Autowired
	public AdminController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping
	public ResponseEntity<?> getUserList() throws SQLException {
		try {
			List<User> userList = userService.selectAll();

			return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> delete(@PathVariable String userId, HttpSession session) {
		try {
			User sessionUser = (User) session.getAttribute("userInfo");
			if (sessionUser != null) { // 로그인 정보가 있다면
				String isManager = sessionUser.getManager();
				if (isManager.equals("T")) { // 권한이 있는 경우
					userService.delete(userId);
					return new ResponseEntity<String>("success", HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("unauthorized", HttpStatus.UNAUTHORIZED);
				}
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{userId}/{auth}")
	public ResponseEntity<?> changePermission(@PathVariable String userId, @PathVariable String auth) {
		try {

			System.out.println(userId+" " +auth);
			userService.changePermission(userId, auth);
			System.out.println("########");
			List<User> userList = userService.selectAll();
			for (User u : userList) {
				System.out.println(u.toString());
			}
			return new ResponseEntity<String>("success", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
