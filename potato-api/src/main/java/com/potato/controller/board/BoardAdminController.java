package com.potato.controller.board;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardAdminService;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.request.UpdateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.potato.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@RestController
public class BoardAdminController {

    private final BoardAdminService boardAdminService;

    @Auth(role = ORGANIZATION_ADMIN)
    @PostMapping("/api/v1/board/{subDomain}")
    public ApiResponse<BoardInfoResponse> createBoard(@PathVariable String subDomain, @Valid @RequestBody CreateBoardRequest request, @MemberId Long memberId) {
        return ApiResponse.of(boardAdminService.createBoard(subDomain, request, memberId));
    }

    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/board/{boardId}")
    public ApiResponse<BoardInfoResponse> updatePublicBoard(@PathVariable Long boardId,@Valid @RequestBody CreateBoardRequest request) {
        return ApiResponse.of(boardAdminService.updatePublicBoard(boardId, request));
    }

}
