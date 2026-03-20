package com.ssafy.live.admin.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.live.admin.dto.User;
import com.ssafy.live.admin.model.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public List<User> selectAll() throws Exception {
		return userMapper.selectAll();
	}

	@Override
	public void delete(String userId) throws Exception {
		userMapper.delete(userId);
	}

	@Override
	public void changePermission(String userId, String auth) throws Exception {
		userMapper.changePermission(userId, auth);
	}
}
