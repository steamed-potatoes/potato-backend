package com.potato.service.organization;

import com.potato.domain.organization.OrganizationRepository;
import com.potato.service.organization.dto.response.OrganizationInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public List<OrganizationInfoResponse> retrieveOrganization() {
        return organizationRepository.findAll().stream()
            .map(OrganizationInfoResponse::of)
            .collect(Collectors.toList());
    }

}
