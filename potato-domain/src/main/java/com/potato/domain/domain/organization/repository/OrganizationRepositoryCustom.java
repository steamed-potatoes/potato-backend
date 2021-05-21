package com.potato.domain.domain.organization.repository;

import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.Organization;

import java.util.List;

public interface OrganizationRepositoryCustom {

    Organization findOrganizationBySubDomain(String subDomain);

    List<Organization> findAllByMemberId(Long memberId);

    List<Organization> findAllByFollowMemberId(Long followerMemberId);

    List<Organization> findAllOrderByFollowersCountWithLimit(int size);

    List<Organization> findAllByCategoryAndLessThanIdOrderByIdDescWithLimit(OrganizationCategory category, Long lastOrganizationId, int size);

}
