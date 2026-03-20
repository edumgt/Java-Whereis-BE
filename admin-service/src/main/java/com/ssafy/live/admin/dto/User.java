package com.ssafy.live.admin.dto;

import lombok.Data;

@Data
public class User {
	private String userId;
	private String userName;
	private String userPwd;
	private String emailId;
	private String emailDomain;
	private String manager;
}
