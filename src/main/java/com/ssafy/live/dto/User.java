package com.ssafy.live.dto;

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
