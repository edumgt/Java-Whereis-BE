package com.ssafy.live.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.live.dto.User;

public interface UserService {
	// 회원가입
	int regist(User user) throws Exception;

	// 로그인
	User login(Map<String, String> map) throws Exception;

	// 회원 정보 수정
	int update(User user) throws Exception;

	// 회원 정보 확인
	User confirm(String userId) throws Exception;

	// 회원 정보 삭제
	void delete(String userId) throws Exception;

	// 회원 id 찾기
	String findId(User user) throws Exception;

	// 회원 pwd 찾기
	String findPwd(User user) throws Exception;
	
	//회원 전체 조회
	List<User> selectAll() throws Exception;
	
	//권한 바꾸기
	void changePermission(String userId,String auth) throws Exception;
}
