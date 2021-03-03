package com.potato.domain.board.repository;

import com.potato.domain.board.Board;
import com.potato.domain.board.Visible;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.board.QBoard.*;

@RequiredArgsConstructor
public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Board findPublicBoardById(Long boardId) {
        return queryFactory.selectFrom(board)
            .where(
                board.id.eq(boardId),
                board.visible.eq(Visible.PUBLIC)
            )
            .fetchOne();
    }

    @Override
    public List<Board> findPublicBoardsOrderByIdDesc(int size) {
        return queryFactory.selectFrom(board)
            .where(
                board.visible.eq(Visible.PUBLIC)
            )
            .orderBy(board.id.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<Board> findPublicBoardsLessThanOrderByIdDescLimit(Long lastBoardId, int size) {
        return queryFactory.selectFrom(board)
            .where(
                board.id.lt(lastBoardId),
                board.visible.eq(Visible.PUBLIC)
            )
            .orderBy(board.id.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public Board findBoardByIdAndSubDomain(Long boardId, String subDomain) {
        return queryFactory.selectFrom(board)
            .where(
                board.id.eq(boardId),
                board.subDomain.eq(subDomain)
            ).fetchOne();
    }

}
