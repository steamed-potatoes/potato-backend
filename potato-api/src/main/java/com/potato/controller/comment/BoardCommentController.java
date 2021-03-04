package com.potato.controller.comment;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.comment.BoardCommentService;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.potato.config.interceptor.auth.Auth.Role.USER;

@RequiredArgsConstructor
@RestController
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Auth(role = USER)
    @PostMapping("/api/v1/board/comment")
    public ApiResponse<String> addBoardComment(@Valid @RequestBody AddBoardCommentRequest request, @MemberId Long memberId) {
        boardCommentService.addBoardComment(request, memberId);
        return ApiResponse.OK;
    }

    @GetMapping("/api/v1/board/comment/list")
    public List<BoardCommentResponse> retrieveCommentsInBoard(@RequestParam Long boardId) {
        return boardCommentService.retrieveBoardCommentList(boardId);
    }

}
