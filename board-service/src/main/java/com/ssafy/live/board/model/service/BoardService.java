package com.ssafy.live.board.model.service;

import java.util.List;

import com.ssafy.live.board.dto.Board;

public interface BoardService {
	int writeBoard(Board board) throws Exception;
	int updateBoard(Board board) throws Exception;
	List<Board> getBoardList() throws Exception;
	Board getDetailList(int articleNo) throws Exception;
	int deleteList(int articleNo) throws Exception;
	void updateHit(int articleNo) throws Exception;
	List<Board> getBoardSearch(String keyword) throws Exception;
}
