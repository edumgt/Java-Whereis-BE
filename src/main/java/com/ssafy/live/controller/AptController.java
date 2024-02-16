package com.ssafy.live.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

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

import com.ssafy.live.dto.DongCode;
import com.ssafy.live.dto.House;
import com.ssafy.live.dto.MyHouse;
import com.ssafy.live.dto.User;
import com.ssafy.live.model.service.HouseService;

@RestController
@RequestMapping("/apts")
public class AptController {

	@Autowired
	HouseService houseService;

	@GetMapping()
	public ResponseEntity<?> getApartInfo(@RequestParam String regcode, String userId) throws SQLException {
		// 관심 매물 저장
		List<House> myHouseList = houseService.selectAllMyHouse(userId);
		List<String> ckAptCodeList = new ArrayList<String>();

		for (House house : myHouseList) {
			ckAptCodeList.add(house.getAptCode());
		}

		List<House> houseList = houseService.getApartList(regcode);
		int size = houseList.size();
		Map<String, Object> li = new HashMap<String, Object>();
		li.put("homes", houseList);
		li.put("aptcode", ckAptCodeList);
		if (size != 0) {
			li.put("empty", "F");
		} else {
			li.put("empty", "T");
		}
		return new ResponseEntity<Map<String, Object>>(li, HttpStatus.OK);
	}
	@GetMapping("/guguns")
	public ResponseEntity<?> getGugunsApartInfo(@RequestParam String regcode, String userId) throws SQLException {
		// 관심 매물 저장
		List<House> myHouseList = houseService.selectAllMyHouse(userId);
		List<String> ckAptCodeList = new ArrayList<String>();

		for (House house : myHouseList) {
			ckAptCodeList.add(house.getAptCode());
		}

		List<House> houseList = houseService.getGugunsApartList(regcode+"%");
		int size = houseList.size();
		Map<String, Object> li = new HashMap<String, Object>();
		li.put("homes", houseList);
		li.put("aptcode", ckAptCodeList);
		if (size != 0) {
			li.put("empty", "F");
		} else {
			li.put("empty", "T");
		}
		return new ResponseEntity<Map<String, Object>>(li, HttpStatus.OK);
	}

	@GetMapping("/ck/{id}")
	public ResponseEntity<?> getCkApartInfo(@PathVariable String id) throws SQLException {
		try {
			List<House> ckList = houseService.selectAllMyHouse(id);
			return new ResponseEntity<List<House>>(ckList, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
				return new ResponseEntity<String>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/ck/{id}/{aptCode}")
	public ResponseEntity<?> getCkApartInfo(@PathVariable String id, @PathVariable String aptCode) throws SQLException {
		try {
			System.out.println("id : " + id);
			System.out.println("aptCode: " + aptCode);
			int cnt = houseService.delMyHouse(aptCode, id);
			if (cnt == 1) {
				return new ResponseEntity<String>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
