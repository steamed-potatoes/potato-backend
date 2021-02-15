package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationServiceUtils {

    public static void validateNotExistsOrganization(OrganizationRepository organizationRepository, String subDomain) {
        Organization organization = organizationRepository.findOrganizationBySubDomain(subDomain);
        if (organization != null) {
            throw new IllegalArgumentException(String.format("이미 존재하는 Organization의 SubDomain(%s) 입니다.", subDomain));
        }
    }

    public static Organization findOrganizationBySubDomain(OrganizationRepository organizationRepository, String subDomain) {
        Organization organization = organizationRepository.findOrganizationBySubDomain(subDomain);
        if (organization == null) {
            throw new IllegalArgumentException(String.format("이미 존재하는 Organization의 SubDomain(%s) 입니다.", subDomain));
        }
        return organization;
    }

}
