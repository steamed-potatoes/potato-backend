package com.potato.controller.organization;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.OrganizationService;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.response.OrganizationDetailInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrganizationController {

    private final OrganizationService organizationService;

    @Auth
    @PostMapping("/api/v1/organization")
    public ApiResponse<OrganizationInfoResponse> createOrganization(
        @Valid @RequestBody CreateOrganizationRequest request, @MemberId Long memberId) {
        return ApiResponse.of(organizationService.createOrganization(request, memberId));
    }

    @GetMapping("/api/v1/organization/{subDomain}")
    public ApiResponse<OrganizationDetailInfoResponse> getDetailOrganizationInfo(@PathVariable String subDomain) {
        return ApiResponse.of(organizationService.getDetailOrganizationInfo(subDomain));
    }

    @GetMapping("/api/v1/organization/list")
    public ApiResponse<List<OrganizationInfoResponse>> getOrganizations() {
        return ApiResponse.of(organizationService.getOrganizationsInfo());
    }

    @Auth
    @GetMapping("/api/v1/organization/my")
    public ApiResponse<List<OrganizationInfoResponse>> getMyOrganizations(@MemberId Long memberId) {
        return ApiResponse.of(organizationService.getMyOrganizationsInfo(memberId));
    }

    @Auth
    @PostMapping("/api/v1/organization/apply/{subDomain}")
    public ApiResponse<String> applyOrganization(@PathVariable String subDomain, @MemberId Long memberId) {
        organizationService.applyOrganization(subDomain, memberId);
        return ApiResponse.OK;
    }

}
