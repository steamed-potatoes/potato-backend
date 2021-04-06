package com.potato.controller.board;

import com.potato.config.interceptor.Auth;
import com.potato.config.resolver.MemberId;
import com.potato.controller.ApiResponse;
import com.potato.service.board.AdminBoardService;
import com.potato.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.service.board.dto.request.DeleteAdminBoardRequest;
import com.potato.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.service.board.dto.response.AdminBoardInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    @Auth
    @PostMapping("/admin/v1/board")
    public ApiResponse<AdminBoardInfoResponse> createAdminBoard(@Valid @RequestBody CreateAdminBoardRequest request, @MemberId Long adminMemberId) {
        return ApiResponse.success(adminBoardService.createAdminBoard(request, adminMemberId));
    }

    @Auth
    @PutMapping("/admin/v1/board")
    public ApiResponse<AdminBoardInfoResponse> updateAdminBoard(@Valid @RequestBody UpdateAdminBoardRequest request, @MemberId Long adminMemberId) {
       return ApiResponse.success(adminBoardService.updateAdminBoard(request));
    }

    @Auth
    @DeleteMapping("/admin/v1/board")
    public ApiResponse<String> deleteAdminBoard(@Valid @RequestBody DeleteAdminBoardRequest request, @MemberId Long adminMemberId) {
        adminBoardService.deleteAdminBoard(request.getAdminBoardId(), adminMemberId);
        return ApiResponse.OK;
    }

}
