package com.ssafy.live.apt.dto;

import java.util.List;

import lombok.Data;

@Data
public class House {
	private String aptCode;
	private int buildYear;
	private String apartmentName;
	private String dong;
	private String jibun;
	private String lng;
	private String lat;
	private String img;
	private List<HouseDeal> houseDeals;
}
