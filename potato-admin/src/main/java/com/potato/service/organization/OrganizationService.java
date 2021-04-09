package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.request.UpdateCategoryRequest;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Transactional(readOnly = true)
    public List<OrganizationInfoResponse> retrieveOrganization() {
        return organizationRepository.findAll().stream()
            .map(OrganizationInfoResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationInfoResponse updateCategory(String subDomain, UpdateCategoryRequest request) {
        Organization organization = OrganizationServiceUtils.findOrganizationBySubDomain(organizationRepository, subDomain);
        organization.updateCategory(request.getCategory());
        return OrganizationInfoResponse.of(organization);
    }

}
