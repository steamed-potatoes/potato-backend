package com.potato.controller.organization;

import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
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

@RequiredArgsConstructor
@RestController
public class OrganizationAdminController {

    private final OrganizationAdminService organizationAdminService;

    @PutMapping("/api/v1/organization/admin/{subDomain}")
    public ApiResponse<OrganizationInfoResponse> updateOrganizationInfo(
        @PathVariable String subDomain, @RequestBody UpdateOrganizationInfoRequest request, @LoginMember MemberSession memberSession) {
        return ApiResponse.of(organizationAdminService.updateOrganizationInfo(subDomain, request, memberSession.getMemberId()));
    }

    @PutMapping("/api/v1/organization/admin/approve/{subDomain}")
    public ApiResponse<String> approveOrganizationMember(
        @PathVariable String subDomain, @RequestBody ManageOrganizationMemberRequest request, @LoginMember MemberSession memberSession) {
        organizationAdminService.approveOrganizationMember(subDomain, request, memberSession.getMemberId());
        return ApiResponse.OK;
    }

    @PutMapping("/api/v1/organization/admin/deny/{subDomain}")
    public ApiResponse<String> denyOrganizationMember(
        @PathVariable String subDomain, @RequestBody ManageOrganizationMemberRequest request, @LoginMember MemberSession memberSession) {
        organizationAdminService.denyOrganizationMember(subDomain, request, memberSession.getMemberId());
        return ApiResponse.OK;
    }

}
