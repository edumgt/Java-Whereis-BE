package com.ssafy.live.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.live.dto.DongCode;
import com.ssafy.live.model.service.DongCodeService;

@RestController
@RequestMapping("/dong-code")
public class DongController {
	
	
	@Autowired
	DongCodeService dongCodeService;
	
	@GetMapping("/sido")
	public ResponseEntity<?> getSido() throws SQLException {
		List<DongCode> dongCodes= dongCodeService.getSidoName();
		Map<String,List<DongCode>> data = new HashMap<String,List<DongCode>>();
		data.put("regcodes",dongCodes);
		return new ResponseEntity<Map<String,List<DongCode>>>(data,HttpStatus.OK);
	}
	
	@GetMapping("/gugun/{sido-code}")
	public ResponseEntity<?> getGugun(@PathVariable("sido-code") String regcode) throws SQLException {
		List<DongCode> dongCodes= dongCodeService.getGugunName(regcode.substring(0, 2));
		Map<String,List<DongCode>> data = new HashMap<String,List<DongCode>>();
		data.put("regcodes",dongCodes);
		return new ResponseEntity<Map<String,List<DongCode>>>(data,HttpStatus.OK);
	}
	
	@GetMapping("/dong/{regcode}")
	public ResponseEntity<?> getDong(@PathVariable("regcode") String regcode) throws SQLException {
		List<DongCode> dongCodes= dongCodeService.getDongName(regcode.substring(0, 5));
		Map<String,List<DongCode>> data = new HashMap<String,List<DongCode>>();
		data.put("regcodes",dongCodes);
		return new ResponseEntity<Map<String,List<DongCode>>>(data,HttpStatus.OK);
	}
}
