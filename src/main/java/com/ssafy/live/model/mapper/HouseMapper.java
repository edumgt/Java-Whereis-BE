package com.ssafy.live.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ssafy.live.dto.House;

public interface HouseMapper {
	// 아파트 매매정보 가져오기 - 아파트 리스트
	List<House> getApartList(@Param("dongCode")String dongCode) throws SQLException;
	// 구군의 아파트 매매정보 가져오기
	List<House> getGugunsApartList(@Param("dongCode")String dongCode) throws SQLException;
	// 관심 아파트 
	List<House> selectAllMyHouse(String user_id) throws SQLException;
	// interest.jsp에서 관심 아파트 삭제
	int delMyHouse(@Param("aptCode") String aptCode, @Param("user_id") String user_id) throws SQLException;
	// 관심 아파트 전체 삭제
	int delAllMyHouse(@Param("user_id") String user_id) throws SQLException;
	// kakaomap.jsp에서 관심 아파트 추가
	int insertMyHouse(@Param("aptCode") String aptCode, @Param("user_id") String user_id) throws SQLException;
}
