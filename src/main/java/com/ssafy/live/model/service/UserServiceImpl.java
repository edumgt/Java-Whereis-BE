package com.ssafy.live.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.live.dto.User;
import com.ssafy.live.model.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	public UserServiceImpl(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public int regist(User user) throws Exception {
		return userMapper.regist(user);
	}

	@Override
	public User login(Map<String, String> map) throws Exception {
		return userMapper.login(map);
	}

	@Override
	public int update(User user) throws Exception {
		return userMapper.update(user);
	}

	@Override
	public User confirm(String userId) throws Exception {
		return userMapper.confirm(userId);
	}

	@Override
	public void delete(String userId) throws Exception {
		userMapper.delete(userId);
	}

	@Override
	public String findId(User user) throws Exception {
		return userMapper.findId(user);
	}

	@Override
	public String findPwd(User user) throws Exception {
		return userMapper.findPwd(user);
	}

	@Override
	public List<User> selectAll() throws Exception {
		return userMapper.selectAll();
	}

	@Override
	public void changePermission(String userId, String auth) throws Exception {
		// TODO Auto-generated method stub

		userMapper.changePermission(userId, auth);

	}

}
