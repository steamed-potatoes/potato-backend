package com.potato.api.controller.board.organization;

import com.potato.api.config.argumentResolver.MemberId;
import com.potato.api.config.interceptor.auth.Auth;
import com.potato.api.controller.ApiResponse;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardWithOrganizationResponse;
import com.potato.api.service.board.organization.OrganizationBoardRetrieveService;
import com.potato.api.service.board.organization.dto.request.*;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponse;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardInfoResponseWithImage;
import com.potato.api.service.board.organization.dto.response.OrganizationBoardWithCreatorInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.potato.api.config.interceptor.auth.Auth.Role.OPTIONAL_LOGIN;

@RequiredArgsConstructor
@RestController
public class OrganizationBoardRetrieveController {

    private final OrganizationBoardRetrieveService organizationBoardRetrieveService;

    @Operation(summary = "특정 그룹 게시물을 조회하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth(role = OPTIONAL_LOGIN)
    @GetMapping("/api/v2/organization/board")
    public ApiResponse<OrganizationBoardWithCreatorInfoResponse> retrieveBoardWithOrganizationAndCreator(@RequestParam Long organizationBoardId, @MemberId Long memberId) {
        return ApiResponse.success(organizationBoardRetrieveService.retrieveBoardWithOrganizationAndCreator(organizationBoardId, memberId));
    }

    @Operation(summary = "전체 그룹 게시물을 스크롤 페이지네이션 기반으로 조회하는 API")
    @GetMapping("/api/v2/organization/board/list")
    public ApiResponse<List<OrganizationBoardWithOrganizationResponse>> retrieveLatestOrganizationBoardList(@Valid RetrieveLatestBoardsRequest request) {
        return ApiResponse.success(organizationBoardRetrieveService.retrieveBoardsWithPagination(request.getType(), request.getLastOrganizationBoardId(), request.getSize()));
    }

    @Operation(summary = "얼마 남지 않은 게시물을 조회하는 API", description = "dateTime = 현재 시간, size = 불러올 개수")
    @GetMapping("/api/v2/organization/board/list/imminentBoards")
    public ApiResponse<List<OrganizationBoardInfoResponseWithImage>> retrieveImminentBoards(@Valid RetrieveImminentBoardsRequest request) {
        return ApiResponse.success(organizationBoardRetrieveService.retrieveImminentBoards(request));
    }

    @Operation(summary = "인기있는 게시물 조회하는 API")
    @GetMapping("/api/v2/organization/board/popular/list")
    public ApiResponse<List<OrganizationBoardInfoResponse>> retrievePopularBoard(@Valid RetrievePopularOrganizationBoardRequest request) {
        return ApiResponse.success(organizationBoardRetrieveService.retrievePopularBoard(request.getSize()));
    }

    @Operation(summary = "특정 그룹의 게시물을 스크롤 페이지네이션 기반으로 조회하는 API")
    @GetMapping("/api/v2/organization/board/list/in/{subDomain}")
    public ApiResponse<List<OrganizationBoardWithOrganizationResponse>> getBoardsInOrganization(@PathVariable String subDomain, @Valid RetrieveLatestBoardsRequest request) {
        return ApiResponse.success(organizationBoardRetrieveService.getBoardsInOrganization(subDomain, request.getType(), request.getLastOrganizationBoardId(), request.getSize()));
    }

}
