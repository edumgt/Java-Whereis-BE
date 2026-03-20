package com.ssafy.live.apt.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.live.apt.dto.DongCode;

@Mapper
public interface DongCodeMapper {
	List<DongCode> getSidoName() throws SQLException;
	List<DongCode> getGugunName(String sidoCode) throws SQLException;
	List<DongCode> getDongName(String code) throws SQLException;
}
