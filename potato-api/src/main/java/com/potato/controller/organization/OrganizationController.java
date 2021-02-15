package com.potato.controller.organization;

import com.potato.config.argumentResolver.LoginMember;
import com.potato.config.session.MemberSession;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.OrganizationService;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.request.UpdateOrganizationInfoRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PostMapping("/api/v1/organization")
    public ApiResponse<OrganizationInfoResponse> createOrganization(
        @Valid @RequestBody CreateOrganizationRequest request, @LoginMember MemberSession memberSession) {
        return ApiResponse.of(organizationService.createOrganization(request, memberSession.getMemberId()));
    }

    @GetMapping("/api/v1/organization/{subDomain}")
    public ApiResponse<OrganizationInfoResponse> getSimpleOrganizationInfo(@PathVariable String subDomain) {
        return ApiResponse.of(organizationService.getSimpleOrganizationInfo(subDomain));
    }

    @GetMapping("/api/v1/organizations")
    public ApiResponse<List<OrganizationInfoResponse>> getOrganizations() {
        return ApiResponse.of(organizationService.getOrganizationsInfo());
    }

    @Operation(security = {@SecurityRequirement(name = "bearer-key")})
    @PutMapping("/api/v1/organization/{subDomain}")
    public ApiResponse<OrganizationInfoResponse> updateOrganizationInfo(
        @PathVariable String subDomain, @RequestBody UpdateOrganizationInfoRequest request, @LoginMember MemberSession memberSession) {
        return ApiResponse.of(organizationService.updateOrganizationInfo(subDomain, request, memberSession.getMemberId()));
    }

}
