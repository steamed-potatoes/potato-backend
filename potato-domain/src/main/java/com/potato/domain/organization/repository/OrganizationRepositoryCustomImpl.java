package com.potato.domain.organization.repository;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationRole;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.organization.QOrganization.organization;
import static com.potato.domain.organization.QOrganizationMemberMapper.organizationMemberMapper;

@RequiredArgsConstructor
public class OrganizationRepositoryCustomImpl implements OrganizationRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Organization findOrganizationBySubDomain(String subDomain) {
        return queryFactory.selectFrom(organization)
            .innerJoin(organization.organizationMemberMapperList, organizationMemberMapper).fetchJoin()
            .where(
                organization.subDomain.eq(subDomain)
            ).fetchOne();
    }

    @Override
    public List<Organization> findAllByMemberId(Long memberId) {
        return queryFactory.selectFrom(organization).distinct()
            .innerJoin(organization.organizationMemberMapperList, organizationMemberMapper).fetchJoin()
            .where(
                organizationMemberMapper.memberId.eq(memberId),
                organizationMemberMapper.role.ne(OrganizationRole.PENDING)
            ).fetch();
    }

}
