package com.potato.controller.comment;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.domain.board.BoardType;
import com.potato.service.comment.BoardCommentService;
import com.potato.service.comment.dto.request.AddBoardCommentRequest;
import com.potato.service.comment.dto.request.LikeBoardCommentRequest;
import com.potato.service.comment.dto.request.UpdateBoardCommentRequest;
import com.potato.service.comment.dto.response.BoardCommentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BoardCommentController {

    private final BoardCommentService boardCommentService;

    @Operation(summary = "게시물에 댓글을 추가합니다.", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v2/board/comment")
    public ApiResponse<String> addBoardComment(@Valid @RequestBody AddBoardCommentRequest request, @MemberId Long memberId) {
        boardCommentService.addBoardComment(request, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 댓글 리스트를 조회합니다.")
    @GetMapping("/api/v2/board/comment/list")
    public ApiResponse<List<BoardCommentResponse>> retrieveBoardComments(@RequestParam BoardType type, @RequestParam Long boardId) {
        return ApiResponse.success(boardCommentService.retrieveBoardCommentList(type, boardId));
    }

    @Operation(summary = "게시물의 댓글을 수정합니다.", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PutMapping("/api/v2/board/comment")
    public ApiResponse<String> updateBoardComment(@RequestBody UpdateBoardCommentRequest request, @MemberId Long memberId) {
        boardCommentService.updateBoardComment(request, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "작성한 댓글을 삭제합니다.", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @DeleteMapping("/api/v2/board/comment")
    public ApiResponse<String> deleteBoardComment(@RequestParam Long boardCommentId, @MemberId Long memberId) {
        boardCommentService.deleteBoardComment(boardCommentId, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "댓글에 좋아요를 합니다.", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v2/board/comment/like")
    public ApiResponse<String> likeBoardComment(@Valid @RequestBody LikeBoardCommentRequest request, @MemberId Long memberId) {
        boardCommentService.likeBoardComment(request.getBoardCommentId(), memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "댓글에 좋아요를 취소합니다.", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @DeleteMapping("/api/v2/board/comment/like")
    public ApiResponse<String> unlikeBoardComment(@Valid LikeBoardCommentRequest request, @MemberId Long memberId) {
        boardCommentService.unLikeBoardComment(request.getBoardCommentId(), memberId);
        return ApiResponse.OK;
    }

}
