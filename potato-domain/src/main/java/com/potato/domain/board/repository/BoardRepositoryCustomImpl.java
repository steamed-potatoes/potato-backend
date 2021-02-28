package com.potato.domain.board.repository;

import com.potato.domain.board.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.potato.domain.board.QBoard.*;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Board findBoardById(Long boardId) {
        return queryFactory.selectFrom(board)
            .where(board.id.eq(boardId))
            .fetchOne();
    }

}
