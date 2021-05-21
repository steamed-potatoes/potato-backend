package com.potato.api.controller.board.organization;

import com.potato.api.config.argumentResolver.MemberId;
import com.potato.api.config.interceptor.auth.Auth;
import com.potato.api.controller.ApiResponse;
import com.potato.api.service.board.organization.OrganizationBoardService;
import com.potato.api.service.board.organization.dto.request.*;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponseWithImage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.potato.api.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@RestController
public class OrganizationBoardController {

    private final OrganizationBoardService organizationBoardService;

    @Operation(summary = "그룹의 관리자가 새로운 그룹의 게시물을 등록하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth(role = ORGANIZATION_ADMIN)
    @PostMapping("/api/v2/organization/board/{subDomain}")
    public ApiResponse<OrganizationBoardInfoResponseWithImage> createOrganizationBoard(
        @PathVariable String subDomain, @Valid @RequestBody CreateOrganizationBoardRequest request, @MemberId Long memberId) {
        return ApiResponse.success(organizationBoardService.createBoard(subDomain, request, memberId));
    }

    @Operation(summary = "그룹의 관리자가 그룹의 게시물을 수정하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v2/organization/board/{subDomain}")
    public ApiResponse<OrganizationBoardInfoResponseWithImage> updateOrganizationBoard(
        @PathVariable String subDomain, @Valid @RequestBody UpdateOrganizationBoardRequest request, @MemberId Long memberId) {
        return ApiResponse.success(organizationBoardService.updateBoard(subDomain, request, memberId));
    }

    @Operation(summary = "그룹의 관리자가 그룹의 게시물을 삭제하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth(role = ORGANIZATION_ADMIN)
    @DeleteMapping("/api/v2/organization/board/{subDomain}")
    public ApiResponse<String> deleteOrganizationBoard(@PathVariable String subDomain, @Valid DeleteOrganizationBoardRequest request,
                                                       @MemberId Long memberId) {
        organizationBoardService.deleteOrganizationBoard(subDomain, request.getOrganizationBoardId(), memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 좋아요를 추가하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v2/organization/board/like")
    public ApiResponse<String> likeOrganizationBoard(@Valid @RequestBody LikeOrganizationBoardRequest request, @MemberId Long memberId) {
        organizationBoardService.likeOrganizationBoard(request, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 좋아요를 취소하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @DeleteMapping("/api/v2/organization/board/like")
    public ApiResponse<String> cancelOrganizationBoard(@Valid LikeOrganizationBoardRequest request, @MemberId Long memberId) {
        organizationBoardService.cancelLikeOrganizationBoard(request, memberId);
        return ApiResponse.OK;
    }

}
