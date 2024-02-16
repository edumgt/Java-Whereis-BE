package com.ssafy.live.dto;

import lombok.Data;

@Data
public class HouseDeal {
	private String aptCode;
	private String floor;	// 층(housedeal)
	private String dealAmount; // 거래금액(housedeal)
	private int dealYear; 	// 거래 년도(housedeal)
	private int dealMonth; 	// 거래 월(housedeal)
	private String area;	// 면적(housedeal)
}
