package com.potato.controller.board;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardService;
import com.potato.service.board.dto.response.BoardInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.potato.config.interceptor.auth.Auth.Role.USER;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "그룹 게시물 리스트를 스크롤 방식으로 조회하는 API", description = "lastBoardId=가장 마지막에 보여지는 게시물의 id, size=몇 개의 게시물을 받아올 것인지")
    @GetMapping("/api/v1/board/list")
    public ApiResponse<List<BoardInfoResponse>> retrievePublicLatestBoardList(@RequestParam Long lastBoardId, @RequestParam int size) {
        return ApiResponse.of(boardService.retrievePublicLatestBoardList(lastBoardId, size));
    }

    @Operation(summary = "특정 그룹의 게시물을 조회하는 API", description = "전체 공개된 게시물만 확인할 수 있습니다")
    @GetMapping("/api/v1/board/{boardId}")
    public ApiResponse<BoardInfoResponse> getDetailBoard(@PathVariable Long boardId) {
        return ApiResponse.of(boardService.getDetailBoard(boardId));
    }

    @Operation(summary = "게시물의 좋아요를 추가하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = USER)
    @PostMapping("/api/v1/board/like/{boardId}")
    public ApiResponse<String> addBoardLike(@PathVariable Long boardId, @MemberId Long memberId) {
        boardService.addBoardLike(boardId, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 좋아요를 취소하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = USER)
    @DeleteMapping("/api/v1/board/like/{boardId}")
    public ApiResponse<String> cancelBoardLike(@PathVariable Long boardId, @MemberId Long memberId) {
        boardService.cancelBoardLike(boardId, memberId);
        return ApiResponse.OK;
    }

}

