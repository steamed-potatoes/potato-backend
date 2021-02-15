package com.potato.domain.organization.repository;

import com.potato.domain.organization.Organization;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.potato.domain.organization.QOrganization.organization;

@RequiredArgsConstructor
public class OrganizationRepositoryCustomImpl implements OrganizationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Organization findOrganizationBySubDomain(String subDomain) {
        return queryFactory.selectFrom(organization)
            .where(
                organization.subDomain.eq(subDomain)
            ).fetchOne();
    }

}
