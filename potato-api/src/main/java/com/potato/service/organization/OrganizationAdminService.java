package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.request.ManageOrganizationMemberRequest;
import com.potato.service.organization.dto.request.UpdateOrganizationInfoRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationAdminService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationInfoResponse updateOrganizationInfo(String subDomain, UpdateOrganizationInfoRequest request, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.validateAdminMember(memberId);
        organization.updateInfo(request.getName(), request.getDescription(), request.getProfileUrl());
        return OrganizationInfoResponse.of(organization);
    }

    @Transactional
    public void approveOrganizationMember(String subDomain, ManageOrganizationMemberRequest request, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.validateAdminMember(memberId);
        organization.approveMember(request.getTargetMemberId());
    }

    @Transactional
    public void denyOrganizationMember(String subDomain, ManageOrganizationMemberRequest request, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.validateAdminMember(memberId);
        organization.denyMember(request.getTargetMemberId());
    }

}
