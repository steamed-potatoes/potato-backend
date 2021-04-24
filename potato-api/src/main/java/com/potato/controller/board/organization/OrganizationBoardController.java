package com.potato.controller.board.organization;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.board.organization.OrganizationBoardRetrieveService;
import com.potato.service.board.organization.OrganizationBoardService;
import com.potato.service.board.organization.dto.request.*;
import com.potato.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.potato.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@RestController
public class OrganizationBoardController {

    private final OrganizationBoardService organizationBoardService;
    private final OrganizationBoardRetrieveService organizationBoardRetrieveService;

    @Operation(summary = "그룹의 관리자가 새로운 그룹의 게시물을 등록하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PostMapping("/api/v2/organization/board/{subDomain}")
    public ApiResponse<OrganizationBoardInfoResponse> createOrganizationBoard(
        @PathVariable String subDomain, @Valid @RequestBody CreateOrganizationBoardRequest request, @MemberId Long memberId) {
        return ApiResponse.success(organizationBoardService.createBoard(subDomain, request, memberId));
    }

    @Operation(summary = "특정 그룹의 게시물을 조회하는 API")
    @GetMapping("/api/v2/organization/board")
    public ApiResponse<OrganizationBoardWithCreatorInfoResponse> retrieveOrganizationBoard(@RequestParam Long organizationBoardId) {
        return ApiResponse.success(organizationBoardRetrieveService.retrieveBoard(organizationBoardId));
    }

    @Operation(summary = "그룹의 게시물을 스크롤 페이지네이션 기반으로 조회하는 API", description = "lastOrganizationBoardId = 가장 마지막 게시물의 id, size = 받아올 게시물의 개수")
    @GetMapping("/api/v2/organization/board/list")
    public ApiResponse<List<OrganizationBoardInfoResponse>> retrieveLatestOrganizationBoardList(@Valid RetrieveLatestBoardsRequest request) {
        return ApiResponse.success(organizationBoardRetrieveService.retrieveBoardsWithPagination(request.getLastOrganizationBoardId(), request.getSize()));
    }

    @Operation(summary = "얼마 남지 않은 게시물을 조회하는 API", description = "dateTime = 현재 시간, size = 불러올 개수")
    @GetMapping("/api/v2/organization/board/list/imminentBoards")
    public ApiResponse<List<OrganizationBoardInfoResponse>> retrieveImminentBoards(RetrieveImminentBoardsRequest request) {
        return ApiResponse.success(organizationBoardRetrieveService.retrieveImminentBoards(request));
    }

    @Operation(summary = "그룹의 관리자가 그룹의 게시물을 수정하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v2/organization/board/{subDomain}")
    public ApiResponse<OrganizationBoardInfoResponse> updateOrganizationBoard(
        @PathVariable String subDomain, @Valid @RequestBody UpdateOrganizationBoardRequest request, @MemberId Long memberId) {
        return ApiResponse.success(organizationBoardService.updateBoard(subDomain, request, memberId));
    }

    @Operation(summary = "그룹의 관리자가 그룹의 게시물을 삭제하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @DeleteMapping("/api/v2/organization/board/{subDomain}")
    public ApiResponse<String> deleteOrganizationBoard(@PathVariable String subDomain, @Valid DeleteOrganizationBoardRequest request,
                                                       @MemberId Long memberId) {
        organizationBoardService.deleteOrganizationBoard(subDomain, request.getOrganizationBoardId(), memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 좋아요를 추가하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @PostMapping("/api/v2/organization/board/like")
    public ApiResponse<String> likeOrganizationBoard(@Valid @RequestBody LikeOrganizationBoardRequest request, @MemberId Long memberId) {
        organizationBoardService.likeOrganizationBoard(request, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "게시물의 좋아요를 취소하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @DeleteMapping("/api/v2/organization/board/like")
    public ApiResponse<String> cancelOrganizationBoard(@Valid LikeOrganizationBoardRequest request, @MemberId Long memberId) {
        organizationBoardService.cancelLikeOrganizationBoard(request, memberId);
        return ApiResponse.OK;
    }

}
