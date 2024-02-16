package com.ssafy.live.model.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ssafy.live.dto.Board;

public interface BoardService {
	
	// 글쓰기
	public int writeBoard(Board board) throws Exception;
	
	// 글 수정
	public int updateBoard(Board board) throws Exception;
	
	// 글 목록
	public List<Board> getBoardList() throws Exception;
	
	// 글 상세확인
	public Board getDetailList(int articleNo) throws Exception;
	
	// 글 삭제
	public int deleteList(int articleNo) throws Exception;
	
	// 조회수 업데이트
	public void updateHit(int articleNo) throws Exception;
	
	// 글 검색
	public List<Board> getBoardSearch(String keyword) throws Exception;
			
}
