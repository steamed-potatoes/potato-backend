package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardCategory;
import com.potato.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.potato.domain.image.QBoardImage;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.potato.domain.board.organization.QOrganizationBoard.organizationBoard;
import static com.potato.domain.image.QBoardImage.boardImage;
import static com.potato.domain.organization.QOrganization.organization;

@RequiredArgsConstructor
public class OrganizationBoardRepositoryCustomImpl implements OrganizationBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public OrganizationBoard findOrganizationBoardById(Long id) {
        return queryFactory.selectFrom(organizationBoard)
            .where(
                organizationBoard.id.eq(id)
            ).fetchOne();
    }

    @Override
    public OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain) {
        return queryFactory.selectFrom(organizationBoard)
            .where(
                organizationBoard.id.eq(organizationBoardId),
                organizationBoard.subDomain.eq(subDomain)
            ).fetchOne();
    }

    @Override
    public List<BoardWithOrganizationDto> findAllWithOrganizationByTypeLessThanOrderByIdDescLimit(String subDomain, OrganizationBoardCategory category, long lastOrganizationBoardId, int size) {
        return queryFactory.select(Projections.fields(BoardWithOrganizationDto.class,
            organizationBoard.subDomain.as("orgSubDomain"),
            organization.name.as("orgName"),
            organization.category.as("orgCategory"),
            organization.profileUrl.as("orgProfileUrl"),
            organization.description.as("orgDescription"),
            organization.membersCount.as("orgMembersCount"),
            organization.followersCount.as("orgFollowersCount"),
            organizationBoard.id.as("boardId"),
            organizationBoard.boardInfo.title.as("boardTitle"),
            organizationBoard.boardInfo.content.as("boardContent"),
            organizationBoard.category.as("boardCategory"),
            organizationBoard.dateTimeInterval.startDateTime.as("boardStartDateTime"),
            organizationBoard.dateTimeInterval.endDateTime.as("boardEndDateTime"),
            organizationBoard.createdDateTime,
            organizationBoard.lastModifiedDateTime
        ))
            .from(organizationBoard)
            .innerJoin(organization).on(organizationBoard.subDomain.eq(organization.subDomain))
            .where(
                eqSubDomain(subDomain),
                eqCategory(category),
                lessThanId(lastOrganizationBoardId)
            )
            .orderBy(organizationBoard.id.desc())
            .limit(size)
            .fetch();
    }

    private BooleanExpression eqSubDomain(String subDomain) {
        if (subDomain == null) {
            return null;
        }
        return organizationBoard.subDomain.eq(subDomain);
    }

    private BooleanExpression eqCategory(OrganizationBoardCategory category) {
        if (category == null) {
            return null;
        }
        return organizationBoard.category.eq(category);
    }

    private BooleanExpression lessThanId(Long organizationBoardId) {
        if (organizationBoardId == 0) {
            return null;
        }
        return organizationBoard.id.lt(organizationBoardId);
    }

    @Override
    public List<OrganizationBoard> findAllBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.selectFrom(organizationBoard)
            .where(
                organizationBoard.dateTimeInterval.startDateTime.before(LocalDateTime.of(endDate, LocalTime.MAX)),
                organizationBoard.dateTimeInterval.endDateTime.after(LocalDateTime.of(startDate, LocalTime.MIN))
            ).fetch();
    }

    @Override
    public List<OrganizationBoard> findAllByBetweenDateTimeWithLimit(LocalDateTime startDateTime, LocalDateTime endDateTime, int size) {
        return queryFactory.selectFrom(organizationBoard)
            .where(
                organizationBoard.dateTimeInterval.endDateTime.between(startDateTime, endDateTime)
            )
            .orderBy(organizationBoard.dateTimeInterval.endDateTime.asc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<OrganizationBoard> findAllOrderByLikesWithLimit(int size) {
        return queryFactory.selectFrom(organizationBoard)
            .orderBy(
                organizationBoard.likesCount.desc(),
                organizationBoard.id.desc()
            )
            .limit(size)
            .fetch();
    }

}
