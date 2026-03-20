package com.ssafy.live.apt.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.live.apt.dto.House;
import com.ssafy.live.apt.model.service.HouseService;

@RestController
@RequestMapping("/apts")
public class AptController {

	@Autowired
	HouseService houseService;

	@GetMapping()
	public ResponseEntity<?> getApartInfo(@RequestParam String regcode, String userId) throws SQLException {
		List<House> myHouseList = houseService.selectAllMyHouse(userId);
		List<String> ckAptCodeList = new ArrayList<>();
		for (House house : myHouseList) {
			ckAptCodeList.add(house.getAptCode());
		}
		List<House> houseList = houseService.getApartList(regcode);
		Map<String, Object> li = new HashMap<>();
		li.put("homes", houseList);
		li.put("aptcode", ckAptCodeList);
		li.put("empty", houseList.isEmpty() ? "T" : "F");
		return new ResponseEntity<>(li, HttpStatus.OK);
	}

	@GetMapping("/guguns")
	public ResponseEntity<?> getGugunsApartInfo(@RequestParam String regcode, String userId) throws SQLException {
		List<House> myHouseList = houseService.selectAllMyHouse(userId);
		List<String> ckAptCodeList = new ArrayList<>();
		for (House house : myHouseList) {
			ckAptCodeList.add(house.getAptCode());
		}
		List<House> houseList = houseService.getGugunsApartList(regcode + "%");
		Map<String, Object> li = new HashMap<>();
		li.put("homes", houseList);
		li.put("aptcode", ckAptCodeList);
		li.put("empty", houseList.isEmpty() ? "T" : "F");
		return new ResponseEntity<>(li, HttpStatus.OK);
	}

	@GetMapping("/ck/{id}")
	public ResponseEntity<?> getCkApartInfo(@PathVariable String id) throws SQLException {
		try {
			List<House> ckList = houseService.selectAllMyHouse(id);
			return new ResponseEntity<>(ckList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/ck/{id}")
	public ResponseEntity<?> insertCkApartInfo(@RequestBody List<String> aptCodes, @PathVariable String id)
			throws SQLException {
		try {
			houseService.delAllMyHouse(id);
			int cnt = 0;
			for (String ckAptCode : aptCodes) {
				cnt += houseService.insertMyHouse(ckAptCode, id);
			}
			if (cnt > 0) {
				return new ResponseEntity<>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/ck/{id}/{aptCode}")
	public ResponseEntity<?> deleteCkApartInfo(@PathVariable String id, @PathVariable String aptCode)
			throws SQLException {
		try {
			int cnt = houseService.delMyHouse(aptCode, id);
			if (cnt == 1) {
				return new ResponseEntity<>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
