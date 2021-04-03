package com.potato.controller.organization;

import com.potato.config.interceptor.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.OrganizationService;
import com.potato.service.organization.dto.request.UpdateCategoryRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @Auth
    @GetMapping("/admin/v1/organization/list")
    public ApiResponse<List<OrganizationInfoResponse>> retrieveOrganization() {
        return ApiResponse.success(organizationService.retrieveOrganization());
    }

    @Auth
    @PatchMapping("/admin/v1/organization/category/approved/{subDomain}")
    public ApiResponse<OrganizationInfoResponse> changeCategoryToApproved(@PathVariable String subDomain, @Valid @RequestBody UpdateCategoryRequest request) {
        return ApiResponse.success(organizationService.changeCategoryToApproved(subDomain, request));
    }

}
