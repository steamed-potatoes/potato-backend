package com.potato.admin.controller.board;

import com.potato.admin.service.board.dto.request.CreateAdminBoardRequest;
import com.potato.admin.service.board.dto.request.DeleteOrganizationBoardRequest;
import com.potato.admin.service.board.dto.request.UpdateAdminBoardRequest;
import com.potato.admin.service.board.dto.response.AdminBoardInfoResponse;
import com.potato.admin.config.interceptor.Auth;
import com.potato.admin.config.resolver.AdminId;
import com.potato.admin.controller.ApiResponse;
import com.potato.admin.service.board.AdminBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

    @Operation(summary = "관리자가_게시글을_생성합니다.")
    @Auth
    @PostMapping("/admin/v1/board/admin")
    public ApiResponse<AdminBoardInfoResponse> createAdminBoard(@Valid @RequestBody CreateAdminBoardRequest request, @AdminId Long adminMemberId) {
        return ApiResponse.success(adminBoardService.createAdminBoard(request, adminMemberId));
    }

    @Operation(summary = "관리자가_게시글을_수정합니다.")
    @Auth
    @PutMapping("/admin/v1/board/admin")
    public ApiResponse<AdminBoardInfoResponse> updateAdminBoard(@Valid @RequestBody UpdateAdminBoardRequest request) {
        return ApiResponse.success(adminBoardService.updateAdminBoard(request));
    }

    @Operation(summary = "관리자가 특정 그룹의 게시글을 삭제합니다.")
    @Auth
    @DeleteMapping("/admin/v1/board/organization/{subDomain}")
    public ApiResponse<String> deleteOrganizationBoard(@PathVariable String subDomain, @Valid DeleteOrganizationBoardRequest request, @AdminId Long adminMemberId) {
        adminBoardService.deleteOrganizationBoard(subDomain, request.getOrganizationBoardId(), adminMemberId);
        return ApiResponse.OK;
    }

}
