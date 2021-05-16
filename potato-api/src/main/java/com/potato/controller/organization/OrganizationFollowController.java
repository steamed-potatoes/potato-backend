package com.potato.controller.organization;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.member.dto.response.MemberInfoResponse;
import com.potato.service.organization.OrganizationRetrieveService;
import com.potato.service.organization.OrganizationService;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrganizationFollowController {

    private final OrganizationService organizationService;
    private final OrganizationRetrieveService organizationRetrieveService;

    @Operation(summary = "특정 조직을 팔로우하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PostMapping("/api/v1/organization/follow/{subDomain}")
    public ApiResponse<String> followOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.followOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "특정 조직을 팔로우 취소하는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @DeleteMapping("/api/v1/organization/follow/{subDomain}")
    public ApiResponse<String> unFollowOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.unFollowOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

    @Operation(summary = "그룹을 팔로우한 멤버 리스트를 불러오는 API")
    @GetMapping("/api/v1/organization/follow/{subDomain}")
    public ApiResponse<List<MemberInfoResponse>> getOrganizationFollowMember(@PathVariable String subDomain) {
        return ApiResponse.success(organizationRetrieveService.getOrganizationFollowMember(subDomain));
    }

    @Operation(summary = "내가 팔로우한 그룹들을 가져오는 API", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @GetMapping("/api/v1/member/organization/follow")
    public ApiResponse<List<OrganizationInfoResponse>> retrieveFollowingOrganizations(@MemberId Long memberId) {
        return ApiResponse.success(organizationRetrieveService.retrieveFollowingOrganizations(memberId));
    }
    
}
