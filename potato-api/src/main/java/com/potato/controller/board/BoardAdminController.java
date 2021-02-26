package com.potato.controller.board;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.board.BoardAdminService;
import com.potato.service.board.dto.request.CreateBoardRequest;
import com.potato.service.board.dto.response.BoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class BoardAdminController {

    private final BoardAdminService boardAdminService;

    @Auth(role = Auth.Role.ORGANIZATION_ADMIN)
    @PostMapping("/api/v1/board/{subDomain}")
    public ApiResponse<BoardInfoResponse> createBoard(@PathVariable String subDomain, @Valid @RequestBody CreateBoardRequest request, @MemberId Long memberId) {
        BoardInfoResponse response = boardAdminService.createBoard(subDomain, request, memberId);
        return ApiResponse.of(response);
    }
}
