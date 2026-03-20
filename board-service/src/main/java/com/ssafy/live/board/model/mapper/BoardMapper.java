package com.ssafy.live.board.model.mapper;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.live.board.dto.Board;

@Mapper
public interface BoardMapper {
	int writeBoard(Board board) throws SQLException;
	int updateBoard(Board board) throws SQLException;
	List<Board> getBoardList() throws SQLException;
	Board getDetailList(int articleNo) throws SQLException;
	int deleteList(int articleNo) throws SQLException;
	void updateHit(int articleNo) throws SQLException;
	List<Board> getBoardSearch(String keyword) throws SQLException;
}
