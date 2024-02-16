package com.ssafy.live.dto;

import lombok.Data;

@Data
public class Board {
	private int articleNo;
    private String userId;
    private String subject;
    private String content;
    private String userName;
    private int hit;
    private String registerTime;
    private String bullet;
}
