package com.ssafy.live.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.live.dto.House;
import com.ssafy.live.model.mapper.HouseMapper;

@Service
public class HouseServiceImpl implements HouseService{

	@Autowired
	HouseMapper houseMapper;
	
	@Override
	public List<House> getApartList(String dongCode) throws SQLException {
		return houseMapper.getApartList(dongCode);
	}
	
	@Override
	public List<House> selectAllMyHouse(String user_id) throws SQLException {
		return houseMapper.selectAllMyHouse(user_id);
	}

	@Override
	public int delMyHouse(String aptCode, String user_id) throws SQLException {
		return houseMapper.delMyHouse(aptCode, user_id);
	}
	
	@Override
	public int insertMyHouse(String aptCode, String user_id) throws SQLException {
		
		return houseMapper.insertMyHouse(aptCode, user_id);
	}

	@Override
	public void delAllMyHouse(String user_id) throws SQLException {
		// TODO Auto-generated method stub
		houseMapper.delAllMyHouse(user_id);
	}

	@Override
	public List<House> getGugunsApartList(String dongCode) throws SQLException {
		// TODO Auto-generated method stub
		return houseMapper.getGugunsApartList(dongCode);
	}

}
