package com.potato.domain.board.admin.repository;

import com.potato.domain.board.admin.AdminBoard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.potato.domain.board.QBoard.board;
import static com.potato.domain.board.admin.QAdminBoard.adminBoard;

@RequiredArgsConstructor
public class AdminBoardRepositoryCustomImpl implements AdminBoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AdminBoard> findAllBetweenDate(LocalDate startDate, LocalDate endDate) {
        return queryFactory.selectFrom(adminBoard)
            .innerJoin(adminBoard.board, board).fetchJoin()
            .where(
                board.dateTimeInterval.startDateTime.before(LocalDateTime.of(endDate, LocalTime.MAX)),
                board.dateTimeInterval.endDateTime.after(LocalDateTime.of(startDate, LocalTime.MIN))
            ).fetch();
    }

    @Override
    public AdminBoard findAdminBoardById(Long adminBoardId) {
        return queryFactory.selectFrom(adminBoard)
            .innerJoin(adminBoard.board, board).fetchJoin()
            .where(
                adminBoard.id.eq(adminBoardId)
            )
            .fetchOne();
    }

}
