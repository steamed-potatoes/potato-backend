package com.potato.controller.organization;

import com.potato.config.argumentResolver.MemberId;
import com.potato.config.interceptor.auth.Auth;
import com.potato.controller.ApiResponse;
import com.potato.service.organization.OrganizationService;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.response.OrganizationDetailInfoResponse;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class OrganizationController {

    private final OrganizationService organizationService;

    @Operation(summary = "새로운 그룹을 생성하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @PostMapping("/api/v1/organization")
    public ApiResponse<OrganizationInfoResponse> createOrganization(
        @Valid @RequestBody CreateOrganizationRequest request, @MemberId Long memberId) {
        return ApiResponse.of(organizationService.createOrganization(request, memberId));
    }

    @Operation(summary = "특정 그룹의 정보를 조회하는 API", description = "해당하는 그룹의 subDomain")
    @GetMapping("/api/v1/organization/{subDomain}")
    public ApiResponse<OrganizationDetailInfoResponse> getDetailOrganizationInfo(@PathVariable String subDomain) {
        return ApiResponse.of(organizationService.getDetailOrganizationInfo(subDomain));
    }

    @Operation(summary = "등록된 그룹 리스트를 불러오는 API")
    @GetMapping("/api/v1/organization/list")
    public ApiResponse<List<OrganizationInfoResponse>> getOrganizations() {
        return ApiResponse.of(organizationService.getOrganizationsInfo());
    }

    @Operation(summary = "내가 속한 그룹의 리스트를 불러오는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @GetMapping("/api/v1/organization/my")
    public ApiResponse<List<OrganizationInfoResponse>> getMyOrganizations(@MemberId Long memberId) {
        return ApiResponse.of(organizationService.getMyOrganizationsInfo(memberId));
    }

    @Operation(summary = "특정 그룹에 가입신청을 하는 API", description = "Bearer 토큰이 필요합니다")
    @Auth
    @PostMapping("/api/v1/organization/join/apply{subDomain}")
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

}
