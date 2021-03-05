package com.potato.controller.organization;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.OrganizationService;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.request.FollowRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.potato.config.interceptor.auth.Auth.Role.ORGANIZATION_MEMBER;

@RequiredArgsConstructor
@RestController
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "새로운 그룹을 생성하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @PostMapping("/api/v1/organization")
    public ApiResponse<OrganizationInfoResponse> createOrganization(@Valid @RequestBody CreateOrganizationRequest request, @MemberId Long memberId) {
        return ApiResponse.of(organizationService.createOrganization(request, memberId));
    }

    @Operation(summary = "특정 그룹에 가입신청을 하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @PostMapping("/api/v1/organization/join/apply/{subDomain}")
    public ApiResponse<String> applyJoiningOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.applyJoiningOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "특정 그룹에 가입신청을 취소 하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @PutMapping("/api/v1/organization/join/cancel/{subDomain}")
    public ApiResponse<String> cancelJoiningOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.cancelJoiningOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "특정 그룹에서 탈퇴하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_MEMBER)
    @DeleteMapping("/api/v1/organization/leave/{subDomain}")
    public ApiResponse<String> leaveFromOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.leaveFromOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "특정 조직을 팔로우하는 API", description = "Bearer 토큰이 필요합니다.")
    @Auth
    @PostMapping("/api/v1/organization/follow")
    public ApiResponse<String> followOrganization(@Valid @RequestBody FollowRequest request, @MemberId Long memberId) {
        organizationService.followOrganization(request, memberId);
        return ApiResponse.OK;
    }

}
