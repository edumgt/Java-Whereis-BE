package com.ssafy.live.apt.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.live.apt.dto.House;

public interface HouseService {
	List<House> getApartList(String dongCode) throws SQLException;
	List<House> getGugunsApartList(String dongCode) throws SQLException;
	List<House> selectAllMyHouse(String user_id) throws SQLException;
	int delMyHouse(String aptCode, String user_id) throws SQLException;
	void delAllMyHouse(String user_id) throws SQLException;
	int insertMyHouse(String aptCode, String user_id) throws SQLException;
}
