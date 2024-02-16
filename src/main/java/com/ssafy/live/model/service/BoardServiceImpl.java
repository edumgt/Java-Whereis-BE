package com.ssafy.live.model.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.live.dto.Board;
import com.ssafy.live.model.mapper.BoardMapper;
import com.ssafy.live.model.mapper.UserMapper;

@Service
public class BoardServiceImpl implements BoardService{
	
	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	public BoardServiceImpl(BoardMapper boardMapper) {
		super();
		this.boardMapper = boardMapper;
	}

	// 글쓰기
	@Override
	public int writeBoard(Board board) throws SQLException {
		return boardMapper.writeBoard(board);
	}

	// 글 수정
	@Override
	public int updateBoard(Board board) throws SQLException {
		return boardMapper.updateBoard(board);
	}

	// 글 목록
	@Override
	public List<Board> getBoardList() throws Exception {
		return boardMapper.getBoardList();
	}

	// 글 상세확인
	@Override
	public Board getDetailList(int articleNo) throws SQLException {
		return boardMapper.getDetailList(articleNo);
	}

	// 글 삭제
	@Override
	public int deleteList(int articleNo) throws Exception {
		return boardMapper.deleteList(articleNo);
	}

	// 조회수 업데이트
	@Override
	public void updateHit(int articleNo) throws SQLException {
		boardMapper.updateHit(articleNo);
	}
	
	// 글 검색
	@Override
	public List<Board> getBoardSearch(String keyword) throws Exception {
		return boardMapper.getBoardSearch(keyword);
	}

}
