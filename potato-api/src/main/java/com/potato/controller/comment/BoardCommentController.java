package com.potato.controller.comment;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.comment.BoardCommentService;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.potato.config.interceptor.auth.Auth.Role.USER;

@RequiredArgsConstructor
@RestController
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "게시물에 댓글을 추가합니다.", description = "Bearer 토큰이 필요합니다")
    @Auth(role = USER)
    @PostMapping("/api/v1/board/comment")
    public ApiResponse<String> addBoardComment(@Valid @RequestBody AddBoardCommentRequest request, @MemberId Long memberId) {
        boardCommentService.addBoardComment(request, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 댓글 리스트를 조회합니다.", description = "Bearer 토큰이 필요합니다")
    @Auth(role = USER)
    @GetMapping("/api/v1/board/comment/list")
    public List<BoardCommentResponse> retrieveCommentsInBoard(@RequestParam Long boardId, @MemberId Long memberId) {
        return boardCommentService.retrieveBoardCommentList(boardId, memberId);
    }

}
