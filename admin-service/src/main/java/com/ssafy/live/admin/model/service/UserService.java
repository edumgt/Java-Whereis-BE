package com.ssafy.live.admin.model.service;

import java.util.List;

import com.ssafy.live.admin.dto.User;

public interface UserService {
	List<User> selectAll() throws Exception;
	void delete(String userId) throws Exception;
	void changePermission(String userId, String auth) throws Exception;
}
