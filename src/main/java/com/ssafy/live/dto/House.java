package com.ssafy.live.dto;

import java.util.List;

import lombok.Data;

@Data
public class House {
	private String aptCode;	// 아파트 코드	(houseinfo, housedeal)
	private int buildYear; 	// 설립년도(houseinfo)
	private String apartmentName; // 아파트 이름(houseinfo)
	private String dong;	// 동(houseinfo)
	private String jibun; 	// 지번(houseinfo)
	private String lng;	// 위도(houseinfo)
	private String lat;	// 경도(houseinfo)
	private String img; // 이미지경로(houseinfo)
	private List<HouseDeal> houseDeals;
}
