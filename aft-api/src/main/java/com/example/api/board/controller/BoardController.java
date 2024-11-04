package com.example.api.board.controller;

import com.example.api.board.service.BoardService;
import com.example.auth.config.UserIdx;
import com.example.domain.payload.board.request.BoardRequest;
import com.example.domain.payload.board.request.BoardSearchRequest;
import com.example.domain.payload.board.response.BoardDetailResponse;
import com.example.domain.payload.board.response.BoardResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    /**
     * 게시판 목록
     * BoardSearchRequest 검색 조건
     * Board Status Y 상태의 게시판만 조회
     *
     * @return 게시판 목록 응답
     */
    @GetMapping("/list")
    public ResponseEntity<List<BoardResponse>> getBoardList(@UserIdx Long userIdx, @ModelAttribute BoardSearchRequest searchRequest) {
        List<BoardResponse> boardList = boardService.getBoardList(userIdx, searchRequest);
        return ResponseEntity.ok(boardList);
    }

    /**
     * 게시판 상세 조회
     *
     * @param boardIdx 게시판 IDX
     * @return 게시판 상세 응답
     */
    @GetMapping("/{boardIdx}")
    public ResponseEntity<BoardDetailResponse> getBoardDetail(@PathVariable Long boardIdx) {
        BoardDetailResponse boardDetail = boardService.getBoardDetail(boardIdx);
        return ResponseEntity.ok(boardDetail);
    }

    /**
     * 게시판 등록
     * 로그인이 되어있는 사용자만 등록 가능
     *
     * @param request 게시판 등록 요청
     * @return 게시판 등록 응답
     */
    @PostMapping("")
    public ResponseEntity<BoardDetailResponse> createBoard(@UserIdx Long userIdx, @RequestBody BoardRequest request) {
        return ResponseEntity.ok(boardService.createBoard(userIdx, request));
    }

    /**
     * 게시판 수정
     * 작성자 본인만 수정 가능
     *
     * @param boardIdx 게시판 IDX
     * @param request  게시판 수정 요청
     * @return 게시판 수정 응답
     */
    @PutMapping("/{boardIdx}")
    public ResponseEntity<BoardDetailResponse> updateBoard(@UserIdx Long userIdx, @PathVariable Long boardIdx, @RequestBody BoardRequest request) {
        return ResponseEntity.ok(boardService.updateBoard(userIdx, boardIdx, request));
    }

    /**
     * 게시판 삭제
     * 작성자 본인만 삭제 가능
     *
     * @param boardIdx 게시판 IDX
     * @return 게시판 삭제 응답
     */
    @DeleteMapping("/{boardIdx}")
    public ResponseEntity<Void> deleteBoard(@UserIdx Long userIdx, @PathVariable Long boardIdx) {
        boardService.deleteBoard(userIdx, boardIdx);
        return ResponseEntity.ok().build();
    }

}