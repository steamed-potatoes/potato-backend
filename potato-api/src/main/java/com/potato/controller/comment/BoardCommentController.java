package com.potato.controller.comment;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.comment.OrganizationBoardCommentService;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardCommentController {

    private final OrganizationBoardCommentService organizationBoardCommentService;

    @Operation(summary = "게시물에 댓글을 추가합니다.", description = "Bearer 토큰이 필요합니다")
    @Auth
    @PostMapping("/api/v2/board/comment")
    public ApiResponse<String> addBoardComment(@Valid @RequestBody AddBoardCommentRequest request, @MemberId Long memberId) {
        organizationBoardCommentService.addBoardComment(request, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 댓글 리스트를 조회합니다.")
    @GetMapping("/api/v2/board/comment/list")
    public List<BoardCommentResponse> retrieveBoardComments(@RequestParam Long organizationBoardId) {
        return organizationBoardCommentService.retrieveBoardCommentList(organizationBoardId);
    }

    @Operation(summary = "작성한 댓글을 삭제합니다.", description = "Bearer 토큰이 필요합니다")
    @Auth
    @DeleteMapping("/api/v2/board/comment")
    public ApiResponse<String> deleteBoardComment(@RequestParam Long boardCommentId, @MemberId Long memberId) {
        organizationBoardCommentService.deleteBoardComment(boardCommentId, memberId);
        return ApiResponse.OK;
    }

}
