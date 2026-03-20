package com.ssafy.live.apt.model.service;

import java.sql.SQLException;
import java.util.List;

import com.ssafy.live.apt.dto.DongCode;

public interface DongCodeService {
	List<DongCode> getSidoName() throws SQLException;
	List<DongCode> getGugunName(String sidoCode) throws SQLException;
	List<DongCode> getDongName(String code) throws SQLException;
}
