package com.potato.api.service.organization;

import com.potato.domain.domain.organization.Organization;
import com.potato.domain.domain.organization.OrganizationRepository;
import com.potato.api.service.organization.dto.request.ManageOrganizationMemberRequest;
import com.potato.api.service.organization.dto.request.UpdateOrganizationInfoRequest;
import com.potato.api.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationAdminService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationInfoResponse updateOrganizationInfo(String subDomain, UpdateOrganizationInfoRequest request) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.updateInfo(request.getName(), request.getDescription(), request.getProfileUrl());
        return OrganizationInfoResponse.of(organization);
    }

    @Transactional
    public void approveOrganizationMember(String subDomain, ManageOrganizationMemberRequest request) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.approvePendingMember(request.getTargetMemberId());
    }

    @Transactional
    public void denyOrganizationMember(String subDomain, ManageOrganizationMemberRequest request) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.denyPendingMember(request.getTargetMemberId());
    }

    @Transactional
    public void kickOffOrganizationUserByAdmin(String subDomain, Long targetMemberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.removeUser(targetMemberId);
    }

    @Transactional
    public void appointOrganizationAdmin(String subDomain, Long targetMemberId, Long adminMemberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.appointOrganizationAdmin(targetMemberId, adminMemberId);
    }

}
