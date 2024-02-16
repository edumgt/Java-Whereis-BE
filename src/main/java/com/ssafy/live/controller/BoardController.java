package com.ssafy.live.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ssafy.live.dto.Board;
import com.ssafy.live.dto.User;
import com.ssafy.live.model.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {

	private final BoardService boardService;

	@Autowired
	public BoardController(BoardService boardService) {
		super();
		this.boardService = boardService;
	}

	// 공지사항 목록 조회
	@GetMapping
	public ResponseEntity<?> boardListPage() throws Exception {
		try {

			List<Board> boardList = boardService.getBoardList();

			return new ResponseEntity<List<Board>>(boardList, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/search/{word}")
	public ResponseEntity<?> boardSearchPage(@PathVariable("word") String word, HttpSession session) throws Exception {
		System.out.println(word);
		try {

			List<Board> boardList = boardService.getBoardSearch(word);

			for (Board b : boardList)
				System.out.println(b.toString());

			session.setAttribute("searched", boardList);
			return new ResponseEntity<String>("success", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{articleNo}")
	public ResponseEntity<?> boardDetail(@PathVariable("articleNo") String articleNo) throws Exception {
		try {

			Board boardDetail = boardService.getDetailList(Integer.parseInt(articleNo));
			boardDetail.setArticleNo(Integer.parseInt(articleNo));

			boardService.updateHit(Integer.parseInt(articleNo));

			return new ResponseEntity<Board>(boardDetail, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/write")
	public ResponseEntity<?> boardWrite(@RequestBody Board board, Model model) throws Exception {
		try {
			if (board.getBullet().equals("글머리"))
				board.setBullet("일반");
			int cnt = boardService.writeBoard(board);

			if (cnt == 1) {
				return new ResponseEntity<String>("success", HttpStatus.CREATED);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete/{articleNo}")
	public ResponseEntity<?> boardDelete(@PathVariable("articleNo") String articleNo) throws Exception {
		try {
			int cnt = boardService.deleteList(Integer.parseInt(articleNo));
	
			if (cnt == 1) {
				return new ResponseEntity<String>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/modify/{articleNo}")
	public ResponseEntity<?> boardModify(@RequestBody Board board, @PathVariable("articleNo") String articleNo) throws Exception {
		System.out.println(board.toString());
		try {

			board.setArticleNo(Integer.parseInt(articleNo));
			int cnt = boardService.updateBoard(board);
			if (cnt == 1) {
				return new ResponseEntity<String>("success", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
