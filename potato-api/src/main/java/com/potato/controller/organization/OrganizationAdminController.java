package com.potato.controller.organization;

import com.potato.config.interceptor.auth.Auth;
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

import static com.potato.config.interceptor.auth.Auth.Role.ORGANIZATION_ADMIN;

@RequiredArgsConstructor
@RestController
public class OrganizationAdminController {

    private final OrganizationAdminService organizationAdminService;

    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/organization/admin/{subDomain}")
    public ApiResponse<OrganizationInfoResponse> updateOrganizationInfo(
        @PathVariable String subDomain, @Valid @RequestBody UpdateOrganizationInfoRequest request) {
        return ApiResponse.of(organizationAdminService.updateOrganizationInfo(subDomain, request));
    }

    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/organization/admin/approve/{subDomain}")
    public ApiResponse<String> approveOrganizationMember(@PathVariable String subDomain,
                                                         @Valid @RequestBody ManageOrganizationMemberRequest request) {
        organizationAdminService.approveOrganizationMember(subDomain, request);
        return ApiResponse.OK;
    }

    @Auth(role = ORGANIZATION_ADMIN)
    @PutMapping("/api/v1/organization/admin/deny/{subDomain}")
    public ApiResponse<String> denyOrganizationMember(
        @PathVariable String subDomain, @Valid @RequestBody ManageOrganizationMemberRequest request) {
        organizationAdminService.denyOrganizationMember(subDomain, request);
        return ApiResponse.OK;
    }

}
