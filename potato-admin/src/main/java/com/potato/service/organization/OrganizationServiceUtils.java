package com.potato.service.organization;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRepository;
import com.potato.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrganizationServiceUtils {

    public static Organization findOrganizationBySubDomain(OrganizationRepository organizationRepository, String subDomain) {
        Organization organization = organizationRepository.findOrganizationBySubDomain(subDomain);
        if (organization == null) {
            throw new NotFoundException(String.format("존재하지 않는 그룹 (%s)입니다.", subDomain), "존재하지 않는 그룹입니다.");
        }
        return organization;
    }

}
