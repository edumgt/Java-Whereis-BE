package com.ssafy.live.model.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import com.ssafy.live.dto.House;

public interface HouseService {
	// 아파트 매매정보 가져오기 - 아파트 리스트
	List<House> getApartList(String dongCode) throws SQLException;
	List<House> getGugunsApartList(String dongCode) throws SQLException;
	// 관심 아파트 
	List<House> selectAllMyHouse(String user_id) throws SQLException;
	// interest.jsp에서 관심 아파트 삭제
	int delMyHouse(String aptCode, String user_id) throws SQLException;
	void delAllMyHouse(String user_id) throws SQLException;
	// kakaomap.jsp에서 관심 아파트 추가
	int insertMyHouse(String aptCode, String user_id) throws SQLException;
}
