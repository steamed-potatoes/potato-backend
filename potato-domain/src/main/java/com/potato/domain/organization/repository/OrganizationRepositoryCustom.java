package com.potato.domain.organization.repository;

import com.potato.domain.organization.Organization;

import java.util.List;

public interface OrganizationRepositoryCustom {

    Organization findOrganizationBySubDomain(String subDomain);

    List<Organization> findAllByMemberId(Long memberId);

}
