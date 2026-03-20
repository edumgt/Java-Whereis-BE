package com.ssafy.live.apt.dto;

import lombok.Data;

@Data
public class HouseDeal {
	private String aptCode;
	private String floor;
	private String dealAmount;
	private int dealYear;
	private int dealMonth;
	private String area;
}
