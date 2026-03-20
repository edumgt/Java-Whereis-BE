package com.ssafy.live.board.model.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.live.board.dto.Board;
import com.ssafy.live.board.model.mapper.BoardMapper;

@Service
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;

	@Override
	public int writeBoard(Board board) throws SQLException {
		return boardMapper.writeBoard(board);
	}

	@Override
	public int updateBoard(Board board) throws SQLException {
		return boardMapper.updateBoard(board);
	}

	@Override
	public List<Board> getBoardList() throws Exception {
		return boardMapper.getBoardList();
	}

	@Override
	public Board getDetailList(int articleNo) throws SQLException {
		return boardMapper.getDetailList(articleNo);
	}

	@Override
	public int deleteList(int articleNo) throws Exception {
		return boardMapper.deleteList(articleNo);
	}

	@Override
	public void updateHit(int articleNo) throws SQLException {
		boardMapper.updateHit(articleNo);
	}

	@Override
	public List<Board> getBoardSearch(String keyword) throws Exception {
		return boardMapper.getBoardSearch(keyword);
	}
}
