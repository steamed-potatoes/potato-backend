package com.potato.controller.board;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardAdminService;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.request.UpdateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.potato.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@RestController
public class BoardAdminController {

    private final BoardAdminService boardAdminService;

    @Operation(summary = "그룹의 관리자가 새로운 그룹의 게시물을 등록하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PostMapping("/api/v1/board/{subDomain}")
    public ApiResponse<BoardInfoResponse> createBoard(@PathVariable String subDomain, @Valid @RequestBody CreateBoardRequest request, @MemberId Long memberId) {
        return ApiResponse.of(boardAdminService.createBoard(subDomain, request, memberId));
    }

    @Operation(summary = "그룹의 관리자가 그룹의 게시물를 수정하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/board/{subDomain}/{boardId}")
    public ApiResponse<BoardInfoResponse> updatePublicBoard(@PathVariable String subDomain, @PathVariable Long boardId, @Valid @RequestBody UpdateBoardRequest request) {
        return ApiResponse.of(boardAdminService.updateBoard(subDomain, boardId, request));
    }

}
