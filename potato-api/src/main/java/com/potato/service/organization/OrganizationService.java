package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.request.FollowRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationInfoResponse createOrganization(CreateOrganizationRequest request, Long memberId) {
        OrganizationServiceUtils.validateNotExistsOrganization(organizationRepository, request.getSubDomain());
        Organization organization = request.toEntity();
        organization.addAdmin(memberId);
        return OrganizationInfoResponse.of(organizationRepository.save(organization));
    }

    @Transactional()
    public void applyJoiningOrganization(String subDomain, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.addPending(memberId);
    }

    @Transactional
    public void cancelJoiningOrganization(String subDomain, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.denyPendingMember(memberId);
    }

    @Transactional
    public void leaveFromOrganization(String subDomain, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.removeUser(memberId);
    }

    @Transactional
    public void followOrganization(FollowRequest request, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, request.getSubDomain());
        organization.addFollow(memberId);
    }

}
