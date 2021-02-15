package com.potato.domain.organization.repository;

import com.potato.domain.organization.Organization;

public interface OrganizationRepositoryCustom {

	Organization findOrganizationBySubDomain(String subDomain);

}
