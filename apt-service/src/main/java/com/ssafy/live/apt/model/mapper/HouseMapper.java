package com.ssafy.live.apt.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.live.apt.dto.House;

@Mapper
public interface HouseMapper {
	List<House> getApartList(@Param("dongCode") String dongCode) throws SQLException;
	List<House> getGugunsApartList(@Param("dongCode") String dongCode) throws SQLException;
	List<House> selectAllMyHouse(String user_id) throws SQLException;
	int delMyHouse(@Param("aptCode") String aptCode, @Param("user_id") String user_id) throws SQLException;
	int delAllMyHouse(@Param("user_id") String user_id) throws SQLException;
	int insertMyHouse(@Param("aptCode") String aptCode, @Param("user_id") String user_id) throws SQLException;
}
