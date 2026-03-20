package com.ssafy.live.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.live.board.dto.Board;
import com.ssafy.live.board.model.service.BoardService;

@RestController
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}

	@GetMapping
	public ResponseEntity<?> boardList() throws Exception {
		try {
			List<Board> boardList = boardService.getBoardList();
			return new ResponseEntity<>(boardList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/search/{word}")
	public ResponseEntity<?> boardSearch(@PathVariable("word") String word) throws Exception {
		try {
			List<Board> boardList = boardService.getBoardSearch(word);
			return new ResponseEntity<>(boardList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{articleNo}")
	public ResponseEntity<?> boardDetail(@PathVariable("articleNo") String articleNo) throws Exception {
		try {
			Board boardDetail = boardService.getDetailList(Integer.parseInt(articleNo));
			boardDetail.setArticleNo(Integer.parseInt(articleNo));
			boardService.updateHit(Integer.parseInt(articleNo));
			return new ResponseEntity<>(boardDetail, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/write")
	public ResponseEntity<?> boardWrite(@RequestBody Board board) throws Exception {
		try {
			if ("글머리".equals(board.getBullet())) board.setBullet("일반");
			int cnt = boardService.writeBoard(board);
			if (cnt == 1) {
				return new ResponseEntity<>("success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{articleNo}")
	public ResponseEntity<?> boardDelete(@PathVariable("articleNo") String articleNo) throws Exception {
		try {
			int cnt = boardService.deleteList(Integer.parseInt(articleNo));
			if (cnt == 1) {
				return new ResponseEntity<>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/modify/{articleNo}")
	public ResponseEntity<?> boardModify(@RequestBody Board board, @PathVariable("articleNo") String articleNo)
			throws Exception {
		try {
			board.setArticleNo(Integer.parseInt(articleNo));
			int cnt = boardService.updateBoard(board);
			if (cnt == 1) {
				return new ResponseEntity<>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
