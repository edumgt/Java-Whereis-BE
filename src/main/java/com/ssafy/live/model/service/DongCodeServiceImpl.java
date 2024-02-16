package com.ssafy.live.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.live.dto.DongCode;
import com.ssafy.live.model.mapper.DongCodeMapper;

@Service
public class DongCodeServiceImpl implements DongCodeService{

	@Autowired
	DongCodeMapper dongCodeMapper;
	
	@Override
	public List<DongCode> getSidoName() throws SQLException {
		return dongCodeMapper.getSidoName();
	}

	@Override
	public List<DongCode> getGugunName(String sidoCode) throws SQLException {
		return dongCodeMapper.getGugunName(sidoCode);
	}

	@Override
	public List<DongCode> getDongName(String code) throws SQLException {
		return dongCodeMapper.getDongName(code);
	}

}
