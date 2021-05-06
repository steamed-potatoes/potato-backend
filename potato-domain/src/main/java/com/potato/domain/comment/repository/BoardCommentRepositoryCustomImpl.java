package com.potato.domain.comment.repository;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.BoardCommentType;
import com.potato.domain.comment.QBoardComment;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.comment.QBoardComment.boardComment;

@RequiredArgsConstructor
public class BoardCommentRepositoryCustomImpl implements BoardCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public BoardComment findBoardCommentByIdAndMemberId(Long boardCommentId, Long memberId) {
        return queryFactory.selectFrom(boardComment)
            .where(
                boardComment.id.eq(boardCommentId),
                eqMemberId(memberId)
            ).fetchOne();
    }

    private BooleanExpression eqMemberId(Long memberId) {
        if (memberId == null) {
            return null;
        }
        return boardComment.memberId.eq(memberId);
    }

    @Override
    public List<BoardComment> findRootCommentByTypeAndBoardId(BoardCommentType type, Long boardId) {
        return queryFactory.selectFrom(boardComment).distinct()
            .leftJoin(boardComment.childComments, new QBoardComment("child")).fetchJoin()
            .where(
                boardComment.type.eq(type),
                boardComment.boardId.eq(boardId),
                boardComment.parentComment.isNull()
            ).fetch();
    }

}
