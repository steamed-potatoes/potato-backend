package com.potato.controller.board;

import com.potato.config.interceptor.Auth;
import com.potato.config.resolver.MemberId;
import com.potato.controller.ApiResponse;
import com.potato.service.board.AdminBoardService;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    @Auth
    @PostMapping("/admin/v1/board")
    public ApiResponse<String> createAdminBoard(@Valid @RequestBody CreateAdminBoardRequest request, @MemberId Long adminMemberId) {
        adminBoardService.createAdminBoard(request, adminMemberId);
        return ApiResponse.OK;
    }

}
