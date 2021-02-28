package com.potato.controller.board;

import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardService;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/v1/board/list/{lastBoardId}")
    public ApiResponse<List<BoardInfoResponse>> retrievePublicLatestBoardList(@PathVariable Long lastBoardId, @RequestParam int size) {
        return ApiResponse.of(boardService.retrievePublicLatestBoardList(lastBoardId, size));
    }

    @GetMapping("/api/v1/board/{boardId}")
    public ApiResponse<BoardInfoResponse> getDetailBoard(@PathVariable Long boardId) {
        return ApiResponse.of(boardService.getDetailBoard(boardId));
    }

}

