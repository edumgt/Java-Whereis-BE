package com.ssafy.live.admin.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.live.admin.dto.User;

@Mapper
public interface UserMapper {
	List<User> selectAll() throws SQLException;
	void delete(String userId) throws SQLException;
	void changePermission(@Param("userId") String userId, @Param("manager") String manager) throws SQLException;
}
