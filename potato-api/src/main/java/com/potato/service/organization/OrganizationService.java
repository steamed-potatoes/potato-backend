package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional(readOnly = true)
    public OrganizationInfoResponse getSimpleOrganizationInfo(String subDomain) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        return OrganizationInfoResponse.of(organization);
    }

    @Transactional(readOnly = true)
    public List<OrganizationInfoResponse> getOrganizationsInfo() {
        return organizationRepository.findAll().stream()
            .map(OrganizationInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional()
    public void applyOrganization(String subDomain, Long memberId) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.addPending(memberId);
        organizationRepository.save(organization);
    }
}
