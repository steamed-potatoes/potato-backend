package com.potato.domain.domain.organization.repository;

import com.potato.domain.domain.organization.OrganizationCategory;
import com.potato.domain.domain.organization.OrganizationRole;
import com.potato.domain.domain.organization.Organization;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.domain.organization.QOrganization.organization;
import static com.potato.domain.domain.organization.QOrganizationFollower.organizationFollower;
import static com.potato.domain.domain.organization.QOrganizationMemberMapper.organizationMemberMapper;

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
    public List<Organization> findAllByCategoryAndLessThanIdOrderByIdDescWithLimit(OrganizationCategory category, Long lastOrganizationId, int size) {
        return queryFactory.selectFrom(organization)
            .where(
                lessThanId(lastOrganizationId),
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

    private BooleanExpression lessThanId(Long organizationId) {
        if (organizationId == 0) {
            return null;
        }
        return organization.id.lt(organizationId);
    }

}
