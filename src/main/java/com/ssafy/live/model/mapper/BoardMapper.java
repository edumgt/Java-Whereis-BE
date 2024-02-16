package com.ssafy.live.model.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.ssafy.live.dto.Board;

public interface BoardMapper {
	
	// 글쓰기
	public int writeBoard(Board board) throws SQLException;
	
	// 글 수정
	public int updateBoard(Board board) throws SQLException;
	
	// 글 목록
	public List<Board> getBoardList() throws SQLException;
	
	// 글 상세확인
	public Board getDetailList(int articleNo) throws SQLException;
	
	// 글 삭제
	public int deleteList(int articleNo) throws SQLException;
	
	// 조회수 업데이트
	public void updateHit(int articleNo) throws SQLException;
	
	// 글 제목 검색
	public List<Board> getBoardSearch(String keyword) throws SQLException;
}
