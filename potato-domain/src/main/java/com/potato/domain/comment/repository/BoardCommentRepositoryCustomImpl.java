package com.potato.domain.comment.repository;

import com.potato.domain.comment.BoardComment;
import com.potato.domain.comment.QBoardComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potato.domain.comment.QBoardComment.boardComment;

@RequiredArgsConstructor
public class BoardCommentRepositoryCustomImpl implements BoardCommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public BoardComment findBoardCommentById(Long boardCommentId) {
        return queryFactory.selectFrom(boardComment)
            .where(
                boardComment.id.eq(boardCommentId)
            ).fetchOne();
    }

    @Override
    public List<BoardComment> findRootCommentByOrganizationBoardId(Long organizationBoardId) {
        return queryFactory.selectFrom(boardComment).distinct()
            .leftJoin(boardComment.childComments, new QBoardComment("child")).fetchJoin()
            .where(
                boardComment.organizationBoardId.eq(organizationBoardId),
                boardComment.parentComment.isNull()
            ).fetch();
    }

    @Override
    public BoardComment findBoardCommentByIdAndMemberId(Long boardCommentId, Long memberId) {
        return queryFactory.selectFrom(boardComment)
            .where(
                boardComment.id.eq(boardCommentId),
                boardComment.memberId.eq(memberId)
            ).fetchOne();
    }

}
