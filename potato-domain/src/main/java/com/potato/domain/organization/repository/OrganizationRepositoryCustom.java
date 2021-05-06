package com.potato.domain.organization.repository;

import com.potato.domain.organization.Organization;

import java.util.List;

public interface OrganizationRepositoryCustom {

    Organization findBySubDomain(String subDomain);

    List<Organization> findAllByMemberId(Long memberId);

    List<Organization> findAllByFollowMemberId(Long memberId);

    List<Organization> findOrderByFollowersCountWithLimit(int size);

}
