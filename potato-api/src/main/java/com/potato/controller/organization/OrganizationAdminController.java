package com.potato.controller.organization;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.OrganizationAdminService;
import com.potato.service.organization.dto.request.ManageOrganizationMemberRequest;
import com.potato.service.organization.dto.request.UpdateOrganizationInfoRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class OrganizationAdminController {

    private final OrganizationAdminService organizationAdminService;

    @Auth
    @PutMapping("/api/v1/organization/admin/{subDomain}")
    public ApiResponse<OrganizationInfoResponse> updateOrganizationInfo(
        @PathVariable String subDomain, @Valid @RequestBody UpdateOrganizationInfoRequest request, @MemberId Long memberId) {
        return ApiResponse.of(organizationAdminService.updateOrganizationInfo(subDomain, request, memberId));
    }

    @Auth
    @PutMapping("/api/v1/organization/admin/approve/{subDomain}")
    public ApiResponse<String> approveOrganizationMember(
        @PathVariable String subDomain, @Valid @RequestBody ManageOrganizationMemberRequest request, @MemberId Long memberId) {
        organizationAdminService.approveOrganizationMember(subDomain, request, memberId);
        return ApiResponse.OK;
    }

    @Auth
    @PutMapping("/api/v1/organization/admin/deny/{subDomain}")
    public ApiResponse<String> denyOrganizationMember(
        @PathVariable String subDomain, @Valid @RequestBody ManageOrganizationMemberRequest request, @MemberId Long memberId) {
        organizationAdminService.denyOrganizationMember(subDomain, request, memberId);
        return ApiResponse.OK;
    }

}
