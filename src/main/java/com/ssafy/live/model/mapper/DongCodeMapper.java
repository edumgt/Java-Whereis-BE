package com.ssafy.live.model.mapper;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.live.dto.DongCode;



public interface DongCodeMapper {
	// sido 정보 가져오기
	List<DongCode> getSidoName() throws SQLException;
	// gugun 정보 가져오기
	List<DongCode> getGugunName(String sidoCode) throws SQLException;
	// dong 정보 가져오기
	List<DongCode> getDongName(String code) throws SQLException;
}
