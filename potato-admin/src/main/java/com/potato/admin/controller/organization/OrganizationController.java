package com.potato.admin.controller.organization;

import com.potato.admin.service.organization.dto.request.UpdateCategoryRequest;
import com.potato.admin.service.organization.dto.response.OrganizationInfoResponse;
import com.potato.admin.config.interceptor.Auth;
import com.potato.admin.controller.ApiResponse;
import com.potato.admin.service.organization.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "가입되어 있는 그룹들을 가져온다", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @GetMapping("/admin/v1/organization/list")
    public ApiResponse<List<OrganizationInfoResponse>> retrieveOrganization() {
        return ApiResponse.success(organizationService.retrieveOrganization());
    }

    @Operation(summary = "가입되어 있는 그룹들을 비인준으로 바꾸거나 인준 그룹으로 바꿔준다", security = {@SecurityRequirement(name = "BearerKey")})
    @Auth
    @PutMapping("/admin/v1/organization/{subDomain}/category")
    public ApiResponse<OrganizationInfoResponse> updateCategoryByAdmin(@PathVariable String subDomain, @Valid @RequestBody UpdateCategoryRequest request) {
        return ApiResponse.success(organizationService.updateCategory(subDomain, request));
    }

}
