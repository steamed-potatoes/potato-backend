package com.potato.api.controller.organization;

import com.potato.api.config.argumentResolver.MemberId;
import com.potato.api.config.interceptor.auth.Auth;
import com.potato.api.controller.ApiResponse;
import com.potato.api.service.organization.OrganizationRetrieveService;
import com.potato.api.service.organization.OrganizationService;
import com.potato.api.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.api.service.organization.dto.request.RetrieveOrganizationsWithPaginationRequest;
import com.potato.api.service.organization.dto.request.RetrievePopularOrganizationsRequest;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import com.potato.api.service.organization.dto.response.OrganizationWithMembersInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.potato.api.config.interceptor.auth.Auth.Role.OPTIONAL_LOGIN;
import static com.potato.api.config.interceptor.auth.Auth.Role.ORGANIZATION_MEMBER;

@RequiredArgsConstructor
@RestController
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationRetrieveService organizationRetrieveService;

    @Operation(summary = "새로운 그룹을 생성하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v1/organization")
    public ApiResponse<OrganizationInfoResponse> createOrganization(@Valid @RequestBody CreateOrganizationRequest request, @MemberId Long memberId) {
        return ApiResponse.success(organizationService.createOrganization(request, memberId));
    }

    @Auth(role = OPTIONAL_LOGIN)
    @Operation(summary = "특정 그룹의 정보를 조회하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @GetMapping("/api/v1/organization/{subDomain}")
    public ApiResponse<OrganizationWithMembersInfoResponse> getDetailOrganizationInfo(@PathVariable String subDomain, @MemberId Long memberId) {
        return ApiResponse.success(organizationRetrieveService.getDetailOrganizationInfo(subDomain, memberId));
    }

    @Operation(summary = "등록된 그룹 리스트를 불러오는 API")
    @GetMapping("/api/v1/organization/list")
    public ApiResponse<List<OrganizationInfoResponse>> retrieveOrganizationsWithPagination(@Valid RetrieveOrganizationsWithPaginationRequest request) {
        return ApiResponse.success(organizationRetrieveService.retrieveOrganizationsWithPagination(request.getCategory(), request.getLastOrganizationId(), request.getSize()));
    }

    @Operation(summary = "인기있는 그룹 리스트를 조회한다")
    @GetMapping("/api/v1/organization/list/popular")
    public ApiResponse<List<OrganizationInfoResponse>> retrievePopularOrganizations(@Valid RetrievePopularOrganizationsRequest request) {
        return ApiResponse.success(organizationRetrieveService.retrievePopularOrganizations(request.getSize()));
    }

    @Operation(summary = "내가 속한 그룹의 리스트를 불러오는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @GetMapping("/api/v1/organization/my")
    public ApiResponse<List<OrganizationInfoResponse>> getMyOrganizations(@MemberId Long memberId) {
        return ApiResponse.success(organizationRetrieveService.getMyOrganizationsInfo(memberId));
    }

    @Operation(summary = "특정 그룹에 가입신청을 하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v1/organization/join/apply/{subDomain}")
    public ApiResponse<String> applyJoiningOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.applyJoiningOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "특정 그룹에 가입신청을 취소 하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PutMapping("/api/v1/organization/join/cancel/{subDomain}")
    public ApiResponse<String> cancelJoiningOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.cancelJoiningOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "특정 그룹에서 탈퇴하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth(role = ORGANIZATION_MEMBER)
    @DeleteMapping("/api/v1/organization/leave/{subDomain}")
    public ApiResponse<String> leaveFromOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.leaveFromOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

}
