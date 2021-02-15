package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.request.CreateOrganizationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public void createOrganization(CreateOrganizationRequest request, Long memberId) {
        OrganizationServiceUtils.validateNotExistsOrganization(organizationRepository, request.getSubDomain());
        Organization organization = request.toEntity();
        organization.addAdmin(memberId);
        organizationRepository.save(organization);
    }

}
