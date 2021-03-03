package com.potato.controller.organization;

import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.OrganizationAdminService;
import com.potato.service.organization.dto.request.ManageOrganizationMemberRequest;
import com.potato.service.organization.dto.request.UpdateOrganizationInfoRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.potato.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@RestController
public class OrganizationAdminController {

    private final OrganizationAdminService organizationAdminService;

    @Operation(summary = "그룹 관리자가 그룹의 정보를 수정하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/organization/admin/{subDomain}")
    public ApiResponse<OrganizationInfoResponse> updateOrganizationInfo(
        @PathVariable String subDomain, @Valid @RequestBody UpdateOrganizationInfoRequest request) {
        return ApiResponse.of(organizationAdminService.updateOrganizationInfo(subDomain, request));
    }

    @Operation(summary = "그룹 관리자가 가입 신청을 수락하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/organization/admin/approve/{subDomain}")
    public ApiResponse<String> approveOrganizationMember(@PathVariable String subDomain,
                                                         @Valid @RequestBody ManageOrganizationMemberRequest request) {
        organizationAdminService.approveOrganizationMember(subDomain, request);
        return ApiResponse.OK;
    }

    @Operation(summary = "그룹 관리자가 가입 신청을 거절하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/organization/admin/deny/{subDomain}")
    public ApiResponse<String> denyOrganizationMember(
        @PathVariable String subDomain, @Valid @RequestBody ManageOrganizationMemberRequest request) {
        organizationAdminService.denyOrganizationMember(subDomain, request);
        return ApiResponse.OK;
    }

}
