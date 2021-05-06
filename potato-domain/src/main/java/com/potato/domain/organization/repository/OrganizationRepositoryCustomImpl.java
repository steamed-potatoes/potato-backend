package com.potato.domain.organization.repository;

import com.potato.domain.organization.Organization;
import com.potato.domain.organization.OrganizationCategory;
import com.potato.domain.organization.OrganizationRole;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.organization.QOrganization.organization;
import static com.potato.domain.organization.QOrganizationFollower.organizationFollower;
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

    @Override
    public List<Organization> findAllByFollowMemberId(Long followerMemberId) {
        return queryFactory.selectFrom(organization)
            .innerJoin(organization.organizationFollowerList, organizationFollower).fetchJoin()
            .where(
                organizationFollower.memberId.eq(followerMemberId)
            ).fetch();
    }

    @Override
    public List<Organization> findAllOrderByFollowersCountWithLimit(int size) {
        return queryFactory.selectFrom(organization)
            .orderBy(
                organization.followersCount.desc(),
                organization.id.desc()
            )
            .limit(size)
            .fetch();
    }

    @Override
    public List<Organization> findAllByCategoryOrderByIdDescWithLimit(OrganizationCategory category, int size) {
        return queryFactory.selectFrom(organization)
            .where(
                eqCategory(category)
            )
            .orderBy(organization.id.desc())
            .limit(size)
            .fetch();
    }

    private BooleanExpression eqCategory(OrganizationCategory category) {
        if (category == null) {
            return null;
        }
        return organization.category.eq(category);
    }

}
