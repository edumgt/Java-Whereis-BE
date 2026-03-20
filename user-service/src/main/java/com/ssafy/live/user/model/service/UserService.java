package com.ssafy.live.user.model.service;

import java.util.List;
import java.util.Map;

import com.ssafy.live.user.dto.User;

public interface UserService {
	int regist(User user) throws Exception;
	User login(Map<String, String> map) throws Exception;
	int update(User user) throws Exception;
	User confirm(String userId) throws Exception;
	void delete(String userId) throws Exception;
	String findId(User user) throws Exception;
	String findPwd(User user) throws Exception;
	List<User> selectAll() throws Exception;
	void changePermission(String userId, String auth) throws Exception;
}
