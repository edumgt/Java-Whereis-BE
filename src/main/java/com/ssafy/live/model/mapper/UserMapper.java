package com.ssafy.live.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.live.dto.User;

@Mapper
public interface UserMapper {
	// 회원가입
	int regist(User user) throws SQLException;

	// 로그인
	User login(Map<String, String> map) throws SQLException;

	// 회원 정보 수정
	int update(User user) throws SQLException;

	// 회원 정보 확인
	User confirm(String userId) throws SQLException;

	// 회원 정보 삭제
	void delete(String userId) throws SQLException;

	// 회원 id 찾기
	String findId(User user) throws SQLException;

	// 회원 pwd 찾기
	String findPwd(User user) throws SQLException;
	
	//회원 전체 조회
	List<User> selectAll() throws SQLException;
	
	//권한 바꾸기
	void changePermission(@Param("userId")String userId, @Param("manager") String manager) throws SQLException;
}
