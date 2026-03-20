package com.ssafy.live.user.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.live.user.dto.User;

@Mapper
public interface UserMapper {
	int regist(User user) throws SQLException;
	User login(Map<String, String> map) throws SQLException;
	int update(User user) throws SQLException;
	User confirm(String userId) throws SQLException;
	void delete(String userId) throws SQLException;
	String findId(User user) throws SQLException;
	String findPwd(User user) throws SQLException;
	List<User> selectAll() throws SQLException;
	void changePermission(@Param("userId") String userId, @Param("manager") String manager) throws SQLException;
}
