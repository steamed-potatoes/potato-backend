package com.potato.controller.boardv2;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.boardv2.OrganizationBoardService;
import com.potato.service.boardv2.dto.request.CreateOrganizationBoardRequest;
import com.potato.service.boardv2.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.boardv2.dto.response.OrganizationBoardWithCreatorInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.potato.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@RestController
public class OrganizationBoardController {

    private final OrganizationBoardService organizationBoardService;

    @Operation(summary = "그룹의 관리자가 새로운 그룹의 게시물을 등록하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PostMapping("/api/v2/organization/board/{subDomain}")
    public ApiResponse<OrganizationBoardInfoResponse> createOrganizationBoard(
        @PathVariable String subDomain, @Valid @RequestBody CreateOrganizationBoardRequest request, @MemberId Long memberId) {
        return ApiResponse.of(organizationBoardService.createBoard(subDomain, request, memberId));
    }

    @Operation(summary = "특정 그룹의 게시물을 조회하는 API")
    @GetMapping("/api/v2/organization/board/{organizationBoardId}")
    public ApiResponse<OrganizationBoardWithCreatorInfoResponse> retrieveOrganizationBoard(@PathVariable Long organizationBoardId) {
        return ApiResponse.of(organizationBoardService.retrieveBoard(organizationBoardId));
    }

    // TODO: 그룹의 게시물을 스크롤 페이지네이션 기반으로 조회하는 API

    // TODO: 그룹의 관리자가 그룹의 게시물을 수정하는 API

    // TODO: 그룹의 관리자가 그룹의 게시물을 삭제하는 API

    // TODO: 게시물의 좋아요를 추가하는 API

    // TODO: 게시물의 좋아요를 취소하는 API

}
