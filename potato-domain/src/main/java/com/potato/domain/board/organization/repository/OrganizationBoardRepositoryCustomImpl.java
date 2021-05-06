package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;
import com.potato.domain.board.organization.OrganizationBoardType;
import com.potato.domain.board.organization.repository.dto.BoardWithOrganizationDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.potato.domain.board.QBoard.board;
import static com.potato.domain.board.organization.QOrganizationBoard.organizationBoard;
import static com.potato.domain.organization.QOrganization.organization;

@RequiredArgsConstructor
public class OrganizationBoardRepositoryCustomImpl implements OrganizationBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public OrganizationBoard findOrganizationBoardById(Long id) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, board).fetchJoin()
            .where(
                organizationBoard.id.eq(id)
            ).fetchOne();
    }

    @Override
    public OrganizationBoard findOrganizationBoardByIdAndSubDomain(Long organizationBoardId, String subDomain) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, board).fetchJoin()
            .where(
                organizationBoard.id.eq(organizationBoardId),
                organizationBoard.subDomain.eq(subDomain)
            ).fetchOne();
    }

    @Override
    public List<BoardWithOrganizationDto> findAllBoardsWithOrganizationByTypeLessThanOrderByIdDescLimit(OrganizationBoardType type, long lastOrganizationBoardId, int size) {
        return queryFactory.select(Projections.fields(BoardWithOrganizationDto.class,
            organizationBoard.subDomain.as("orgSubDomain"),
            organization.name.as("orgName"),
            organization.category.as("orgCategory"),
            organization.profileUrl.as("orgProfileUrl"),
            organization.description.as("orgDescription"),
            organization.membersCount.as("orgMembersCount"),
            organization.followersCount.as("orgFollowersCount"),
            organizationBoard.id.as("boardId"),
            organizationBoard.board.title.as("boardTitle"),
            organizationBoard.content.as("boardContent"),
            organizationBoard.imageUrl.as("boardImageUrl"),
            organizationBoard.type.as("boardType"),
            organizationBoard.board.dateTimeInterval.startDateTime.as("boardStartDateTime"),
            organizationBoard.board.dateTimeInterval.endDateTime.as("boardEndDateTime"),
            organizationBoard.createdDateTime,
            organizationBoard.lastModifiedDateTime
        ))
            .from(organizationBoard)
            .innerJoin(organization).on(organizationBoard.subDomain.eq(organization.subDomain))
            .where(
                eqType(type),
                lessThanId(lastOrganizationBoardId)
            )
            .orderBy(organizationBoard.id.desc())
            .limit(size)
            .fetch();
    }

    private BooleanExpression eqType(OrganizationBoardType type) {
        if (type == null) {
            return null;
        }
        return organizationBoard.type.eq(type);
    }

    private BooleanExpression lessThanId(Long organizationBoardId) {
        if (organizationBoardId == 0) {
            return null;
        }
        return organizationBoard.id.lt(organizationBoardId);
    }

    @Override
    public List<OrganizationBoard> findAllOrganizationBoardsBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, board).fetchJoin()
            .where(
                board.dateTimeInterval.startDateTime.before(LocalDateTime.of(endDate, LocalTime.MAX)),
                board.dateTimeInterval.endDateTime.after(LocalDateTime.of(startDate, LocalTime.MIN))
            ).fetch();
    }

    @Override
    public List<OrganizationBoard> findAllOrganizationBoardsBetweenDateTimeWithLimit(LocalDateTime startDateTime, LocalDateTime endDateTime, int size) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, board).fetchJoin()
            .where(
                board.dateTimeInterval.endDateTime.between(startDateTime, endDateTime)
            )
            .orderBy(board.dateTimeInterval.endDateTime.asc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<OrganizationBoard> findAllBoardsOrderByLikesWithLimit(int size) {
        return queryFactory.selectFrom(organizationBoard)
            .orderBy(
                organizationBoard.likesCount.desc(),
                organizationBoard.id.desc()
            )
            .limit(size)
            .fetch();
    }

}
