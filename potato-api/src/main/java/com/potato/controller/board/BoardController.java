package com.potato.controller.board;

import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardService;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/v1/board")
    public ApiResponse<List<BoardInfoResponse>> getBoards() {
        return ApiResponse.of(boardService.getBoards());
    }

}

