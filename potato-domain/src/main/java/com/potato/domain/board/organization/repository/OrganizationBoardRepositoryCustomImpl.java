package com.potato.domain.board.organization.repository;

import com.potato.domain.board.organization.OrganizationBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.potato.domain.board.QBoard.board;
import static com.potato.domain.board.organization.QOrganizationBoard.organizationBoard;

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
    public List<OrganizationBoard> findBoardsOrderByDesc(int size) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, board).fetchJoin()
            .orderBy(organizationBoard.id.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<OrganizationBoard> findBoardsLessThanOrderByIdDescLimit(long lastOrganizationBoardId, int size) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, board).fetchJoin()
            .where(
                organizationBoard.id.lt(lastOrganizationBoardId)
            )
            .orderBy(organizationBoard.id.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<OrganizationBoard> findBetweenDateIncludeOverlapping(LocalDate startDate, LocalDate endDate) {
        return queryFactory.selectFrom(organizationBoard)
            .innerJoin(organizationBoard.board, board).fetchJoin()
            .where(
                board.dateTimeInterval.startDateTime.before(LocalDateTime.of(endDate, LocalTime.MAX)),
                board.dateTimeInterval.endDateTime.after(LocalDateTime.of(startDate, LocalTime.MIN))
            ).fetch();
    }

    @Override
    public List<OrganizationBoard> findBetweenDateLimit(LocalDateTime startDateTime, LocalDateTime endDateTime, int size) {
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
    public List<OrganizationBoard> findPopularBoard() {
        return queryFactory.selectFrom(organizationBoard)
            .orderBy(organizationBoard.likesCount.desc(), organizationBoard.id.desc())
            .limit(5)
            .fetch();
    }

}
