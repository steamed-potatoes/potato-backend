package com.potato.domain.organization.repository;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;

import java.util.List;

public interface OrganizationRepositoryCustom {

    Organization findOrganizationBySubDomain(String subDomain);

    List<Organization> findAllByMemberId(Long memberId);

    List<Organization> findAllByFollowMemberId(Long followerMemberId);

    List<Organization> findAllOrderByFollowersCountWithLimit(int size);

    List<Organization> findAllByCategoryAndLessThanIdOrderByIdDescWithLimit(OrganizationCategory category, Long lastOrganizationId, int size);

}
